package pe.edu.cibertec.tech_mentor.domain.model

data class LessonProgress(
    val idProgresoLeccion: Long,
    val idUsuario: Long,
    val idLeccion: Long,
    val tituloLeccion: String,
    val estado: String,
    val puntaje: Int,
    val intentos: Int,
    val fechaInicio: String?,
    val fechaUltimaActividad: String?,
    val fechaCompletado: String?,
    val xpGanado: Int
)