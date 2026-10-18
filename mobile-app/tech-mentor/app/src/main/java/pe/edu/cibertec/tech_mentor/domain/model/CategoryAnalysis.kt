package pe.edu.cibertec.tech_mentor.domain.model

data class CategoryAnalysis(
    val idCategoriaPregunta: Long,
    val categoria: String,
    val puntajeObtenido: Int,
    val puntajeMaximo: Int,
    val porcentaje: Double
)