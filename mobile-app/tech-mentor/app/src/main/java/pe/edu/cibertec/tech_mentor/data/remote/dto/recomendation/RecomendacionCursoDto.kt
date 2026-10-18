package pe.edu.cibertec.tech_mentor.data.remote.dto.recommendation

data class CourseRecommendationResponseDto(
    val idRecomendacion: Long,
    val idUsuario: Long,
    val idResultado: Long,
    val idCurso: Long,
    val nombreCurso: String?,
    val idProfesion: Long?,
    val nombreProfesion: String?,
    val prioridad: Int?,
    val motivo: String?,
    val origen: String?,
    val explicacionIa: String?,
    val estado: String?,
    val fechaCreacion: String?
)