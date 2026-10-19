package pe.edu.cibertec.tech_mentor.data.remote.dto.catalog

data class LessonResponseDto(
    val idLeccion: Long,
    val idNivel: Long,
    val titulo: String,
    val descripcion: String?,
    val contenido: String?,
    val orden: Int?,
    val xpRecompensa: Int?
)