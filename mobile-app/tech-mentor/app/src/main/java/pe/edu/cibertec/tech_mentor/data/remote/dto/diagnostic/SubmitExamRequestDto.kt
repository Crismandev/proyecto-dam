package pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic

data class SubmitExamRequestDto(
    val respuestas: List<ExamAnswerSubmissionDto>
)

data class ExamAnswerSubmissionDto(
    val idPregunta: Long,
    val idOpcionSeleccionada: Long? = null
)