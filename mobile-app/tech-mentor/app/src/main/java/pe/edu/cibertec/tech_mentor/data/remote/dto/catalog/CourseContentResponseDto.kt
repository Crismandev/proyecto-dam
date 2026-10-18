package pe.edu.cibertec.tech_mentor.data.remote.dto.catalog

data class CourseContentResponseDto(
    val idCurso: Long,
    val nombre: String,
    val descripcion: String?,
    val icono: String?,
    val xpRequerido: Int?,
    val xpRecompensa: Int?,
    val estadoProgreso: String? = null,
    val idNivelActual: Long? = null,
    val xpGanado: Int? = null,
    val niveles: List<LevelContentDto>? = emptyList()
)

data class LevelContentDto(
    val idNivel: Long,
    val idCurso: Long?,
    val nombre: String,
    val descripcion: String?,
    val orden: Int?,
    val lecciones: List<LessonContentDto>? = emptyList()
)

data class LessonContentDto(
    val idLeccion: Long,
    val idNivel: Long?,
    val titulo: String,
    val descripcion: String?,
    val contenido: String?,
    val orden: Int?,
    val xpRecompensa: Int?,
    val estadoProgreso: String? = null,
    val completada: Boolean? = false,
    val recursos: List<LessonResourceDto>? = emptyList(),
    val ejercicios: List<ExerciseContentDto>? = emptyList()
)

data class LessonResourceDto(
    val idRecurso: Long,
    val idLeccion: Long?,
    val tipo: String?,
    val url: String?,
    val plataforma: String?,
    val duracion: Int?,
    val orden: Int?
)

data class ExerciseContentDto(
    val idEjercicio: Long,
    val idLeccion: Long?,
    val tipoEjercicio: String?,
    val enunciado: String,
    val orden: Int?,
    val explicacion: String?,
    val xpRecompensa: Int?,
    val opciones: List<ExerciseOptionDto>? = emptyList()
)

data class ExerciseOptionDto(
    val idOpcion: Long,
    val idEjercicio: Long?,
    val texto: String,
    val orden: Int?
)