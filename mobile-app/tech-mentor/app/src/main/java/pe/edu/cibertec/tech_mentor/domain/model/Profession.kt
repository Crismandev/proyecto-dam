package pe.edu.cibertec.tech_mentor.domain.model

data class Profession(
    val idProfesion: Long,
    val nombre: String,
    val descripcion: String,
    val activo: Boolean
)