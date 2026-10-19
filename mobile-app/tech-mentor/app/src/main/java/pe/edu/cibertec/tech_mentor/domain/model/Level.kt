package pe.edu.cibertec.tech_mentor.domain.model

data class Level(
    val idNivel: Long,
    val idCurso: Long,
    val nombre: String,
    val descripcion: String,
    val orden: Int
)