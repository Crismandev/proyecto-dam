package pe.edu.cibertec.tech_mentor.domain.model

data class ProfessionCourse(
    val idProfesionCurso: Long,
    val idProfesion: Long,
    val idCurso: Long,
    val prioridad: Int,
    val obligatorio: Boolean
)