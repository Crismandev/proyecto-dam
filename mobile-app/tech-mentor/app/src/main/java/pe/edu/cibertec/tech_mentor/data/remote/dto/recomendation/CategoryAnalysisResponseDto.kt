package pe.edu.cibertec.tech_mentor.data.remote.dto.recommendation

data class CategoryAnalysisResponseDto(
    val idCategoriaPregunta: Long,
    val categoria: String?,
    val puntajeObtenido: Int?,
    val puntajeMaximo: Int?,
    val porcentaje: Double?
)