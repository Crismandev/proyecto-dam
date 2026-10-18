package pe.edu.cibertec.tech_mentor.data.remote.dto.catalog

data class CategoryResponseDto(
    val idCategoria: Long,
    val nombre: String,
    val descripcion: String?,
    val icono: String?,
    val activo: Boolean?,
    val fechaCreacion: String?
)