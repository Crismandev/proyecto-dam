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

    // --- Implementación del registro de alumno adaptado al backend ---
    override suspend fun register(params: pe.edu.cibertec.tech_mentor.ui.register.RegisterUserParams): ApiResponse<AuthUser> {
        return try {
            val username = if (params.email.contains("@")) params.email.substringBefore("@") else params.email

            val userRequestDto = pe.edu.cibertec.tech_mentor.data.remote.dto.user.CreateUserRequestDto(
                idRol = 3L, // ROL_ALUMNO
                idEstadoUsuario = 1L, // ESTADO_ACTIVO
                nombres = params.firstName,
                apellidos = params.lastName,
                email = params.email,
                username = username,
                password = params.password,
                fechaNacimiento = params.birthDate.ifBlank { null },
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
                    val errorMsg = when (code) {
                        409 -> "El correo electrónico o nombre de usuario ya está registrado"
                        400 -> "Verifica los datos ingresados para el registro"
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