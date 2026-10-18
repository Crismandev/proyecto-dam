package pe.edu.cibertec.tech_mentor.data.remote.dto.catalog

data class ProfessionCourseResponseDto(
    val idProfesionCurso: Long,
    val idProfesion: Long,
    val idCurso: Long,
    val prioridad: Int?,
    val obligatorio: Boolean?
)