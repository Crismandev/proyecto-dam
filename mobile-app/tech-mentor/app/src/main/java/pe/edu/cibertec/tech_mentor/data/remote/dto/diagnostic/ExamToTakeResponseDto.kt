package pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic

data class ExamToTakeResponseDto(
    val examen: DiagnosticExamResponseDto,
    val preguntas: List<PublicQuestionDto>? = emptyList()
)

data class PublicQuestionDto(
    val idPregunta: Long,
    val idCategoriaPregunta: Long?,
    val enunciado: String,
    val nivelDificultad: String?,
    val tipoPregunta: String?,
    val puntaje: Int?,
    val orden: Int?,
    val opciones: List<PublicDiagnosticOptionDto>? = emptyList()
)

data class PublicDiagnosticOptionDto(
    val idOpcionDiagnostico: Long,
    val texto: String,
    val orden: Int?
)