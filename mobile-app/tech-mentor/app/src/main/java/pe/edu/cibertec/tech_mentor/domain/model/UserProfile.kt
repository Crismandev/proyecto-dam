package pe.edu.cibertec.tech_mentor.domain.model

data class UserProfile(
    val idUsuario: Long,
    val idRol: Long?,
    val nombreRol: String?,
    val idEstadoUsuario: Long?,
    val nombreEstado: String?,
    val nombres: String,
    val apellidos: String,
    val email: String,
    val emailVerificado: Boolean,
    val fechaNacimiento: String?,
    val avatarUrl: String?,
    val biografia: String?,
    val telefono: String?,
    val pais: String?,
    val ciudad: String?,
    val xpTotal: Int,
    val monedas: Int,
    val nivelUsuario: Int,
    val rachaActual: Int,
    val rachaMaxima: Int,
    val fechaRegistro: String?
)