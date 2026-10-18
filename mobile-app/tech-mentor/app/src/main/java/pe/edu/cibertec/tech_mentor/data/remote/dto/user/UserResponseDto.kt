package pe.edu.cibertec.tech_mentor.data.remote.dto.user

data class UserResponseDto(
    val idUsuario: Long,
    val idRol: Long?,
    val nombreRol: String?,
    val idEstadoUsuario: Long?,
    val nombreEstado: String?,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val emailVerificado: Boolean = false,
    val fechaNacimiento: String?,
    val avatarUrl: String?,
    val biografia: String?,
    val telefono: String?,
    val pais: String?,
    val ciudad: String?,
    val xpTotal: Int = 0,
    val monedas: Int = 0,
    val nivelUsuario: Int = 1,
    val rachaActual: Int = 0,
    val rachaMaxima: Int = 0,
    val fechaRegistro: String?
)