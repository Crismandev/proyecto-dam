package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.AuthApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.ChangePasswordRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.LoginRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository
import pe.edu.cibertec.tech_mentor.util.SessionManager

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): ApiResponse<AuthUser> {
        return try {
            val response = authApiService.login(LoginRequestDto(username, password))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                sessionManager.saveAuthToken(body.token)
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

    override fun logout() {
        sessionManager.clearSession()
    }
}