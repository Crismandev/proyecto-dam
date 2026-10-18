package pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic

data class DiagnosticResultResponseDto(
    val idResultado: Long,
    val idUsuario: Long,
    val idExamen: Long,
    val idNivelAsignado: Long?,
    val nombreNivelAsignado: String?,
    val puntajeObtenido: Int?,
    val puntajeMaximo: Int?,
    val porcentaje: Double?,
    val fechaRealizacion: String?
)