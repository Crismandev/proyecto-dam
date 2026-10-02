package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.data.remote.api.AuthApi
import pe.edu.cibertec.tech_mentor.data.remote.dto.LoginRequest
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(correo: String, contrasena: String): Result<String> {
        return try {
            val request = LoginRequest(correo, contrasena)
            val response = authApi.login(request)

            if (response.isSuccessful && response.body() != null) {
                val token = response.body()!!.token
                sessionManager.saveAuthToken(token)
                Result.success("Login exitoso")
            } else {
                Result.failure(Exception("Error en autenticación: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error de red: ${e.message}"))
        }
    }

    override fun logout() {
        sessionManager.clearSession()
    }

    override fun isUserLoggedIn(): Boolean {
        return sessionManager.isUserLoggedIn()
    }
}