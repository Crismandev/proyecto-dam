package pe.edu.cibertec.tech_mentor.data.remote.dto.auth

data class ChangePasswordRequestDto(
    val claveActual: String,
    val nuevaClave: String
)