package pe.edu.cibertec.tech_mentor.data.remote.dto.user

data class AssignProfessionRequestDto(
    val idProfesion: Long,
    val principal: Boolean? = null
)