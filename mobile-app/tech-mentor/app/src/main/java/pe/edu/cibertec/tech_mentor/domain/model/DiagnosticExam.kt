package pe.edu.cibertec.tech_mentor.domain.model

data class DiagnosticExam(
    val idExamen: Long,
    val idCurso: Long?,
    val idProfesion: Long?,
    val nombre: String,
    val descripcion: String,
    val activo: Boolean
)