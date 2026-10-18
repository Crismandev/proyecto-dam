package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.progress.CourseProgressResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.progress.LessonAnswerRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.progress.LessonProgressResponseDto
import pe.edu.cibertec.tech_mentor.domain.model.CourseProgress
import pe.edu.cibertec.tech_mentor.domain.model.LessonAnswer
import pe.edu.cibertec.tech_mentor.domain.model.LessonProgress

fun CourseProgressResponseDto.toDomain(): CourseProgress {
    return CourseProgress(
        idProgreso = this.idProgreso,
        idUsuario = this.idUsuario,
        idCurso = this.idCurso,
        nombreCurso = this.nombreCurso.orEmpty(),
        idNivelActual = this.idNivelActual,
        nombreNivelActual = this.nombreNivelActual,
        fechaInicio = this.fechaInicio,
        fechaUltimaActividad = this.fechaUltimaActividad,
        estado = this.estado ?: "EN_CURSO",
        xpGanado = this.xpGanado ?: 0,
        fechaCompletado = this.fechaCompletado
    )
}

fun LessonProgressResponseDto.toDomain(): LessonProgress {
    return LessonProgress(
        idProgresoLeccion = this.idProgresoLeccion,
        idUsuario = this.idUsuario,
        idLeccion = this.idLeccion,
        tituloLeccion = this.tituloLeccion.orEmpty(),
        estado = this.estado ?: "NO_INICIADO",
        puntaje = this.puntaje ?: 0,
        intentos = this.intentos ?: 0,
        fechaInicio = this.fechaInicio,
        fechaUltimaActividad = this.fechaUltimaActividad,
        fechaCompletado = this.fechaCompletado,
        xpGanado = this.xpGanado ?: 0
    )
}

fun LessonAnswer.toDto(): LessonAnswerRequestDto {
    return LessonAnswerRequestDto(
        idEjercicio = this.idEjercicio,
        idOpcionSeleccionada = this.idOpcionSeleccionada
    )
}