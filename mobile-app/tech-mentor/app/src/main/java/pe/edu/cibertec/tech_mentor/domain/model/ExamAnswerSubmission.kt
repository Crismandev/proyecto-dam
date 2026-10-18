package pe.edu.cibertec.tech_mentor.domain.model

data class ExamAnswerSubmission(
    val idPregunta: Long,
    val idOpcionSeleccionada: Long? = null
)