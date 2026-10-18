package pe.edu.cibertec.tech_mentor.data.remote.dto.progress

data class CourseProgressResponseDto(
    val idProgreso: Long,
    val idUsuario: Long,
    val idCurso: Long,
    val nombreCurso: String? = null,
    val idNivelActual: Long?,
    val nombreNivelActual: String? = null,
    val fechaInicio: String?,
    val fechaUltimaActividad: String?,
    val estado: String?,
    val xpGanado: Int?,
    val fechaCompletado: String?
)