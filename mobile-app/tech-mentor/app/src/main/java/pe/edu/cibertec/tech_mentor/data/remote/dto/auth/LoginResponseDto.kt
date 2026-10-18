package pe.edu.cibertec.tech_mentor.data.remote.dto.auth

data class LoginResponseDto(
    val token: String,
    val tipo: String,
    val idUsuario: Long,
    val username: String,
    val rol: String
)