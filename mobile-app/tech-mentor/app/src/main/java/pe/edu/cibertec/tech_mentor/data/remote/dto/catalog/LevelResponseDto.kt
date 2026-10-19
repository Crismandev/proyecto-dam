package pe.edu.cibertec.tech_mentor.data.remote.dto.catalog

data class LevelResponseDto(
    val idNivel: Long,
    val idCurso: Long,
    val nombre: String,
    val descripcion: String?,
    val orden: Int?
)