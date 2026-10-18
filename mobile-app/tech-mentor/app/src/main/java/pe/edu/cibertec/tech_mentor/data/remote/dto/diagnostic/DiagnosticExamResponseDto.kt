package pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic

data class DiagnosticExamResponseDto(
    val idExamen: Long,
    val idCurso: Long?,
    val idProfesion: Long?,
    val nombre: String,
    val descripcion: String?,
    val activo: Boolean?
)