package pe.edu.cibertec.tech_mentor.data.remote.dto.user

data class UserProfessionResponseDto(
    val idUsuarioProfesion: Long,
    val idUsuario: Long,
    val idProfesion: Long,
    val nombreProfesion: String?,
    val principal: Boolean = false,
    val fechaAsignacion: String?
)