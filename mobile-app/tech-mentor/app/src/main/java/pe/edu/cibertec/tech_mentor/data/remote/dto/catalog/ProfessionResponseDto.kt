package pe.edu.cibertec.tech_mentor.data.remote.dto.catalog

data class ProfessionResponseDto(
    val idProfesion: Long,
    val nombre: String,
    val descripcion: String?,
    val activo: Boolean?
)