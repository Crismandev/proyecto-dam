package pe.edu.cibertec.tech_mentor.domain.model

data class Lesson(
    val idLeccion: Long,
    val idNivel: Long,
    val titulo: String,
    val descripcion: String,
    val contenido: String,
    val orden: Int,
    val xpRecompensa: Int
)