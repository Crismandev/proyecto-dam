package pe.edu.cibertec.tech_mentor.data.remote.dto.progress

data class LessonProgressResponseDto(
    val idProgresoLeccion: Long,
    val idUsuario: Long,
    val idLeccion: Long,
    val tituloLeccion: String? = null,
    val estado: String?,
    val puntaje: Int?,
    val intentos: Int?,
    val fechaInicio: String?,
    val fechaUltimaActividad: String?,
    val fechaCompletado: String?,
    val xpGanado: Int?
)