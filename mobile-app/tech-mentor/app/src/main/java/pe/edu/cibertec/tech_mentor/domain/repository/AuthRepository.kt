package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser

interface AuthRepository {
    suspend fun login(username: String, password: String): ApiResponse<AuthUser>
    // --- Nuevo método para registro de usuarios alumnos adaptado al backend: ---
    suspend fun register(params: pe.edu.cibertec.tech_mentor.ui.register.RegisterUserParams): ApiResponse<AuthUser>
    suspend fun cambiarClave(claveActual: String, nuevaClave: String): ApiResponse<Unit>
    fun logout()
}