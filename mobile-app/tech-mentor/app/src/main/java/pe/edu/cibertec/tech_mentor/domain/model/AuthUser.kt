package pe.edu.cibertec.tech_mentor.domain.model

data class AuthUser(
    val idUsuario: Long,
    val username: String,
    val rol: String,
    val token: String
)