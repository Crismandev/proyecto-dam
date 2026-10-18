package pe.edu.cibertec.tech_mentor.data.remote.dto.progress

data class CompleteLessonRequestDto(
    val respuestas: List<LessonAnswerRequestDto>
)

data class LessonAnswerRequestDto(
    val idEjercicio: Long,
    val idOpcionSeleccionada: Long? = null,
    val respuestaTexto: String? = null
)