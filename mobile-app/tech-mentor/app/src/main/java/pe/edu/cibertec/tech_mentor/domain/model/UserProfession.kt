package pe.edu.cibertec.tech_mentor.domain.model

data class UserProfession(
    val idUsuarioProfesion: Long,
    val idUsuario: Long,
    val idProfesion: Long,
    val nombreProfesion: String?,
    val principal: Boolean,
    val fechaAsignacion: String?
)