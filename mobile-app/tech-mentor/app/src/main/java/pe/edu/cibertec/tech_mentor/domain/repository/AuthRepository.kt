package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser

interface AuthRepository {
    suspend fun login(username: String, password: String): ApiResponse<AuthUser>
    suspend fun cambiarClave(claveActual: String, nuevaClave: String): ApiResponse<Unit>
    fun logout()
}