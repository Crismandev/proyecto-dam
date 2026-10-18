package pe.edu.cibertec.tech_mentor.data.remote.dto.auth

data class CambiarClaveDto(
    val claveActual: String,
    val nuevaClave: String
)