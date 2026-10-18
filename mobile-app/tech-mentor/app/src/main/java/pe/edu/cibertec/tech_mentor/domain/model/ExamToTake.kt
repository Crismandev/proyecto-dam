package pe.edu.cibertec.tech_mentor.domain.model

data class ExamToTake(
    val examen: DiagnosticExam,
    val preguntas: List<PublicQuestion>
)

data class PublicQuestion(
    val idPregunta: Long,
    val idCategoriaPregunta: Long,
    val enunciado: String,
    val nivelDificultad: String,
    val tipoPregunta: String,
    val puntaje: Int,
    val orden: Int,
    val opciones: List<PublicDiagnosticOption>
)

data class PublicDiagnosticOption(
    val idOpcionDiagnostico: Long,
    val texto: String,
    val orden: Int
)