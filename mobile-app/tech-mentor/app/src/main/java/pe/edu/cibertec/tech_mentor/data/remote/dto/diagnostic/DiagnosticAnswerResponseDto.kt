package pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic

data class DiagnosticAnswerResponseDto(
    val idRespuesta: Long,
    val idResultado: Long,
    val idPregunta: Long,
    val idOpcionSeleccionada: Long?,
    val esCorrecta: Boolean?
)