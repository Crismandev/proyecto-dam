package pe.edu.cibertec.tech_mentor.domain.model

data class Category(
    val idCategoria: Long,
    val nombre: String,
    val descripcion: String,
    val icono: String?,
    val activo: Boolean,
    val fechaCreacion: String?
)