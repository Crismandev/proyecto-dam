package pe.edu.cibertec.tech_mentor.data.remote.dto.user

data class CreateUserRequestDto(
    val idRol: Long,
    val idEstadoUsuario: Long? = null,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val username: String,
    val password: String,
    val fechaNacimiento: String? = null,
    val telefono: String? = null,
    val pais: String? = null,
    val ciudad: String? = null
)