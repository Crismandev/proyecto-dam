package pe.edu.cibertec.tech_mentor.domain.model

data class LessonAnswer(
    val idEjercicio: Long,
    val idOpcionSeleccionada: Long? = null
)