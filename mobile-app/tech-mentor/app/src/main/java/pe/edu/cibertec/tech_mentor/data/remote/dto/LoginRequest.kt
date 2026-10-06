package pe.edu.cibertec.tech_mentor.data.remote.dto

data class LoginRequest(
    val correo: String,
    val contrasena: String
)