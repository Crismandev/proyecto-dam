package pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic

data class QuestionCategoryResponseDto(
    val idCategoriaPregunta: Long,
    val nombre: String,
    val descripcion: String?
)