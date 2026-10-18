package pe.edu.cibertec.tech_mentor.domain.model

data class CourseProgress(
    val idProgreso: Long,
    val idUsuario: Long,
    val idCurso: Long,
    val nombreCurso: String,
    val idNivelActual: Long?,
    val nombreNivelActual: String?,
    val fechaInicio: String?,
    val fechaUltimaActividad: String?,
    val estado: String,
    val xpGanado: Int,
    val fechaCompletado: String?
)