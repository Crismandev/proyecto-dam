package pe.edu.cibertec.tech_mentor.domain.model

data class DiagnosticResult(
    val idResultado: Long,
    val idUsuario: Long,
    val idExamen: Long,
    val idNivelAsignado: Long?,
    val nombreNivelAsignado: String?,
    val puntajeObtenido: Int,
    val puntajeMaximo: Int,
    val porcentaje: Double,
    val fechaRealizacion: String?
)