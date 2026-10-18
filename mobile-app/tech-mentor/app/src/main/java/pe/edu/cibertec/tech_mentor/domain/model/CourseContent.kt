package pe.edu.cibertec.tech_mentor.domain.model

data class CourseContent(
    val idCurso: Long,
    val nombre: String,
    val descripcion: String,
    val icono: String?,
    val xpRequerido: Int,
    val xpRecompensa: Int,
    val estadoProgreso: String?,
    val idNivelActual: Long?,
    val xpGanado: Int,
    val niveles: List<LevelContent>
)

data class LevelContent(
    val idNivel: Long,
    val nombre: String,
    val descripcion: String,
    val orden: Int,
    val lecciones: List<LessonContent>
)

data class LessonContent(
    val idLeccion: Long,
    val titulo: String,
    val descripcion: String,
    val contenido: String,
    val orden: Int,
    val xpRecompensa: Int,
    val estadoProgreso: String?,
    val completada: Boolean,
    val recursos: List<LessonResource>,
    val ejercicios: List<ExerciseContent>
)

data class LessonResource(
    val idRecurso: Long,
    val tipo: String,
    val url: String,
    val plataforma: String?,
    val duracion: Int,
    val orden: Int
)

data class ExerciseContent(
    val idEjercicio: Long,
    val tipoEjercicio: String,
    val enunciado: String,
    val orden: Int,
    val explicacion: String?,
    val xpRecompensa: Int,
    val opciones: List<ExerciseOption>
)

data class ExerciseOption(
    val idOpcion: Long,
    val texto: String,
    val orden: Int
)