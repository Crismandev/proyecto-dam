package pe.edu.cibertec.tech_mentor.domain.model

data class DiagnosticAnswer(
    val idRespuesta: Long,
    val idResultado: Long,
    val idPregunta: Long,
    val idOpcionSeleccionada: Long?,
    val esCorrecta: Boolean?
)