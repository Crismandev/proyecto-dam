package pe.edu.cibertec.tech_mentor.domain.model

data class Course(
    val idCurso: Long,
    val idCategoria: Long,
    val nombre: String,
    val descripcion: String,
    val icono: String?,
    val xpRequerido: Int,
    val xpRecompensa: Int,
    val activo: Boolean,
    val fechaCreacion: String?,
    val fechaActualizacion: String?
)