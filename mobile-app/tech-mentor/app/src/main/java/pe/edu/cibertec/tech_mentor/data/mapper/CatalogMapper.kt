package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.*
import pe.edu.cibertec.tech_mentor.domain.model.*

// --- CATEGORÍAS Y CURSOS ---
fun CategoryResponseDto.toDomain(): Category {
    return Category(
        idCategoria = this.idCategoria,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        icono = this.icono,
        activo = this.activo ?: true,
        fechaCreacion = this.fechaCreacion
    )
}

fun CourseResponseDto.toDomain(): Course {
    return Course(
        idCurso = this.idCurso,
        idCategoria = this.idCategoria,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        icono = this.icono,
        xpRequerido = this.xpRequerido ?: 0,
        xpRecompensa = this.xpRecompensa ?: 0,
        activo = this.activo ?: true,
        fechaCreacion = this.fechaCreacion,
        fechaActualizacion = this.fechaActualizacion
    )
}

// --- CONTENIDO DEL CURSO (Árbol de lecciones) ---

fun CourseContentResponseDto.toDomain(): CourseContent {
    return CourseContent(
        idCurso = this.idCurso,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        icono = this.icono,
        xpRequerido = this.xpRequerido ?: 0,
        xpRecompensa = this.xpRecompensa ?: 0,
        estadoProgreso = this.estadoProgreso,
        idNivelActual = this.idNivelActual,
        xpGanado = this.xpGanado ?: 0,
        niveles = this.niveles?.map { it.toDomain() } ?: emptyList()
    )
}

fun LevelContentDto.toDomain(): LevelContent {
    return LevelContent(
        idNivel = this.idNivel,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        orden = this.orden ?: 0,
        lecciones = this.lecciones?.map { it.toDomain() } ?: emptyList()
    )
}

fun LessonContentDto.toDomain(): LessonContent {
    return LessonContent(
        idLeccion = this.idLeccion,
        titulo = this.titulo,
        descripcion = this.descripcion.orEmpty(),
        contenido = this.contenido.orEmpty(),
        orden = this.orden ?: 0,
        xpRecompensa = this.xpRecompensa ?: 0,
        estadoProgreso = this.estadoProgreso,
        completada = this.completada ?: false,
        recursos = this.recursos?.map { it.toDomain() } ?: emptyList(),
        ejercicios = this.ejercicios?.map { it.toDomain() } ?: emptyList()
    )
}

fun LessonResourceDto.toDomain(): LessonResource {
    return LessonResource(
        idRecurso = this.idRecurso,
        tipo = this.tipo.orEmpty(),
        url = this.url.orEmpty(),
        plataforma = this.plataforma,
        duracion = this.duracion ?: 0,
        orden = this.orden ?: 0
    )
}

fun ExerciseContentDto.toDomain(): ExerciseContent {
    return ExerciseContent(
        idEjercicio = this.idEjercicio,
        tipoEjercicio = this.tipoEjercicio.orEmpty(),
        enunciado = this.enunciado,
        orden = this.orden ?: 0,
        explicacion = this.explicacion,
        xpRecompensa = this.xpRecompensa ?: 0,
        opciones = this.opciones?.map { it.toDomain() } ?: emptyList()
    )
}

fun ExerciseOptionDto.toDomain(): ExerciseOption {
    return ExerciseOption(
        idOpcion = this.idOpcion,
        texto = this.texto,
        orden = this.orden ?: 0
    )
}