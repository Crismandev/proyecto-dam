package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.mapper.toDto
import pe.edu.cibertec.tech_mentor.data.remote.api.AuthApi
import pe.edu.cibertec.tech_mentor.data.remote.dto.LoginRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser
import pe.edu.cibertec.tech_mentor.domain.model.RegisterUserParams
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthUser> {
        return try {
            val response = authApi.login(LoginRequestDto(email = email, password = password))
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val authUser = body.toDomain()
                sessionManager.saveAuthToken(authUser.token)
                sessionManager.saveUserId(authUser.idUser)
                sessionManager.setOnboardingSeen()
                Result.success(authUser)
            } else {
                Result.failure(Exception("Login error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    override suspend fun register(params: RegisterUserParams): Result<AuthUser> {
        return try {
            val response = authApi.register(params.toDto())
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val authUser = body.toDomain()
                sessionManager.saveAuthToken(authUser.token)
                sessionManager.saveUserId(authUser.idUser)
                sessionManager.setOnboardingSeen()
                Result.success(authUser)
            } else {
                Result.failure(Exception("Registration error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

    override fun logout() {
        sessionManager.clearSession()
    }

    override fun isUserLoggedIn(): Boolean {
        return sessionManager.isUserLoggedIn()
    }
}