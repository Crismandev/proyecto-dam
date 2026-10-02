package pe.edu.cibertec.tech_mentor.domain.repository

interface AuthRepository {
    suspend fun login(correo: String, contrasena: String): Result<String>
    fun logout()
    fun isUserLoggedIn(): Boolean
}