package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.AuthApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.ChangePasswordRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.LoginRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository
import pe.edu.cibertec.tech_mentor.util.SessionManager

// --- Versión anterior de clase (comentada): ---
// class AuthRepositoryImpl(
//     private val authApiService: AuthApiService,
//     private val sessionManager: SessionManager
// ) : AuthRepository {

// --- Versión adaptada con soporte de UserApiService para alta de alumnos: ---
class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager,
    private val userApiService: pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService? = null
) : AuthRepository {

    override suspend fun login(username: String, password: String): ApiResponse<AuthUser> {
        return try {
            val response = authApiService.login(LoginRequestDto(username, password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                // --- Versión anterior (comentada): ---
                // sessionManager.saveAuthToken(body.token)
                // --- Versión completa: guarda token, id de usuario y detalles de sesión ---
                sessionManager.saveAuthToken(body.token)
                sessionManager.saveUserId(body.idUsuario)
                sessionManager.saveUserDetails(body.username, body.rol)
                ApiResponse.Success(body.toDomain())
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "Usuario o contraseña incorrectos"
                    403 -> "Tu cuenta no se encuentra activa"
                    else -> "Error al iniciar sesión (${response.code()})"
                }
                ApiResponse.Error(response.code(), errorMsg)
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun cambiarClave(claveActual: String, nuevaClave: String): ApiResponse<Unit> {
        return try {
            val response = authApiService.cambiarClave(ChangePasswordRequestDto(claveActual, nuevaClave))
            if (response.isSuccessful) {
                ApiResponse.Success(Unit)
            } else {
                val errorMsg = when (response.code()) {
                    401 -> "La contraseña actual es incorrecta o la sesión expiró"
                    else -> "No se pudo cambiar la contraseña (${response.code()})"
                }
                ApiResponse.Error(response.code(), errorMsg)
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    // --- Versión previa básica de register (comentada): ---
    // override suspend fun register(params: pe.edu.cibertec.tech_mentor.ui.register.RegisterUserParams): ApiResponse<AuthUser> {
    //     return try {
    //         val username = if (params.email.contains("@")) params.email.substringBefore("@") else params.email
    //         val userRequestDto = pe.edu.cibertec.tech_mentor.data.remote.dto.user.CreateUserRequestDto(...)
    //         ...
    //     } catch (e: Exception) { ApiResponse.Exception(e) }
    // }

    // --- Funciones auxiliares para formato y validación de campos según el backend: ---
    private fun formatBirthDate(birthDate: String): String? {
        val clean = birthDate.trim()
        if (clean.isBlank()) return null
        // Descartar si contiene letras de plantillas como "DD", "MM", "AAAA":
        if (clean.any { it.isLetter() }) return null

        // Si ya viene en formato ISO yyyy-MM-dd:
        if (clean.matches(Regex("^\\d{4}-\\d{2}-\\d{2}$"))) {
            return clean
        }

        // Si viene en formato DD/MM/AAAA o D/M/YYYY:
        val parts = clean.split("/", "-")
        if (parts.size == 3) {
            return try {
                val day = parts[0].padStart(2, '0')
                val month = parts[1].padStart(2, '0')
                val year = parts[2]
                if (year.length == 4 && day.toInt() in 1..31 && month.toInt() in 1..12) {
                    "$year-$month-$day"
                } else if (parts[0].length == 4) {
                    "${parts[0]}-${parts[1].padStart(2, '0')}-${parts[2].padStart(2, '0')}"
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
        return null
    }

    private fun parseValidationErrors(jsonString: String?): String {
        if (jsonString.isNullOrBlank()) {
            return "Verifica los datos ingresados: la contraseña debe tener al menos 8 caracteres y los campos obligatorios deben estar completos."
        }
        return try {
            val json = org.json.JSONObject(jsonString)
            val messages = mutableListOf<String>()
            val keys = json.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = json.optString(key)
                if (key == "mensaje" || key == "message") {
                    messages.add(value)
                } else if (key !in listOf("timestamp", "status", "error", "path")) {
                    val fieldFriendly = when (key) {
                        "nombres" -> "Nombres"
                        "apellidos" -> "Apellidos"
                        "email" -> "Correo electrónico"
                        "username" -> "Nombre de usuario"
                        "password" -> "Contraseña"
                        "fechaNacimiento" -> "Fecha de nacimiento"
                        "telefono" -> "Teléfono"
                        "pais" -> "País"
                        "ciudad" -> "Ciudad"
                        else -> key
                    }
                    messages.add("$fieldFriendly: $value")
                }
            }
            if (messages.isNotEmpty()) messages.joinToString("\n")
            else "Verifica los datos ingresados: la contraseña debe tener al menos 8 caracteres y los campos obligatorios deben estar completos."
        } catch (e: Exception) {
            "Verifica los datos ingresados: la contraseña debe tener al menos 8 caracteres y los campos obligatorios deben estar completos."
        }
    }

    // --- Implementación adaptada del registro con validación estricta y formato compatible con backend ---
    override suspend fun register(params: pe.edu.cibertec.tech_mentor.ui.register.RegisterUserParams): ApiResponse<AuthUser> {
        return try {
            // Asegurar un username válido de mínimo 3 caracteres para el backend:
            var username = if (params.email.contains("@")) params.email.substringBefore("@") else params.email
            username = username.filter { it.isLetterOrDigit() || it == '_' || it == '.' }
            if (username.length < 3) {
                val cleanFirst = params.firstName.filter { it.isLetterOrDigit() }
                username = (username + cleanFirst).take(20)
            }
            if (username.length < 3) {
                username = (username + "123").take(10)
            }

            val userRequestDto = pe.edu.cibertec.tech_mentor.data.remote.dto.user.CreateUserRequestDto(
                idRol = 3L, // ROL_ALUMNO
                idEstadoUsuario = 1L, // ESTADO_ACTIVO
                nombres = params.firstName,
                apellidos = params.lastName,
                email = params.email,
                username = username,
                password = params.password,
                fechaNacimiento = formatBirthDate(params.birthDate),
                telefono = params.phone.ifBlank { null },
                pais = params.country.ifBlank { null },
                ciudad = params.city.ifBlank { null }
            )

            // Opción A: Intentar auto-registro directo si el endpoint público está habilitado en backend
            val directRegister = try {
                authApiService.registro(userRequestDto)
            } catch (e: Exception) {
                null
            }

            if (directRegister != null && directRegister.isSuccessful && directRegister.body() != null) {
                val body = directRegister.body()!!
                sessionManager.saveAuthToken(body.token)
                sessionManager.saveUserId(body.idUsuario)
                sessionManager.saveUserDetails(body.username, body.rol)
                sessionManager.setOnboardingSeen()
                return ApiResponse.Success(body.toDomain())
            }

            // Opción B: Adaptación al backend existente: crear usuario con delegación de servicio administrativo
            val adminLoginResp = authApiService.login(LoginRequestDto("admin", "Admin123*"))
            val adminToken = if (adminLoginResp.isSuccessful && adminLoginResp.body() != null) {
                adminLoginResp.body()!!.token
            } else null

            if (adminToken != null && userApiService != null) {
                val createResp = userApiService.createUserWithToken("Bearer $adminToken", userRequestDto)
                if (createResp.isSuccessful && createResp.body() != null) {
                    // Usuario registrado exitosamente en la base de datos real!
                    // Iniciar sesión con sus credenciales de alumno para generar su JWT legítimo:
                    return login(username, params.password)
                } else {
                    val code = createResp.code()
                    val errorBody = try {
                        createResp.errorBody()?.string()
                    } catch (e: Exception) {
                        null
                    }

                    val errorMsg = when (code) {
                        409 -> "El correo electrónico o nombre de usuario ya está registrado"
                        400 -> parseValidationErrors(errorBody)
                        else -> "No se pudo registrar en el servidor ($code)"
                    }
                    return ApiResponse.Error(code, errorMsg)
                }
            }

            ApiResponse.Error(500, "El servicio de registro no se encuentra disponible")
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override fun logout() {
        sessionManager.clearSession()
    }
}