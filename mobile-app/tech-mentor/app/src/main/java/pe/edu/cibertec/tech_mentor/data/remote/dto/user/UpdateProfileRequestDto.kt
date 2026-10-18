package pe.edu.cibertec.tech_mentor.data.remote.dto.user

data class UpdateProfileRequestDto(
    val nombres: String? = null,
    val apellidos: String? = null,
    val fechaNacimiento: String? = null,
    val avatarUrl: String? = null,
    val biografia: String? = null,
    val telefono: String? = null,
    val pais: String? = null,
    val ciudad: String? = null
)