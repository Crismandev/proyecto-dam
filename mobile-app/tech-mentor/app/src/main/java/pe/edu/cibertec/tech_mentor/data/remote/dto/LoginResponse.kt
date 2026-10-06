package pe.edu.cibertec.tech_mentor.data.remote.dto

data class LoginResponse(
    val token: String,
    val idUsuario: Int,
    val nombres: String,
    val correo: String
)