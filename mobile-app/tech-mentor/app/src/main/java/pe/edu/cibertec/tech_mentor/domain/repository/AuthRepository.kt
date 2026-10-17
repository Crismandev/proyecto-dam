package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.domain.model.AuthUser
import pe.edu.cibertec.tech_mentor.domain.model.RegisterUserParams

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthUser>
    suspend fun register(params: RegisterUserParams): Result<AuthUser>
    fun logout()
    fun isUserLoggedIn(): Boolean
}