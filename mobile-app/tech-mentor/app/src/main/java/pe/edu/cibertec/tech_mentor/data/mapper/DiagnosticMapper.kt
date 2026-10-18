package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic.*
import pe.edu.cibertec.tech_mentor.domain.model.*

fun DiagnosticExamResponseDto.toDomain(): DiagnosticExam {
    return DiagnosticExam(
        idExamen = this.idExamen,
        idCurso = this.idCurso,
        idProfesion = this.idProfesion,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        activo = this.activo ?: true
    )
}

fun ExamToTakeResponseDto.toDomain(): ExamToTake {
    return ExamToTake(
        examen = this.examen.toDomain(),
        preguntas = this.preguntas?.map { it.toDomain() } ?: emptyList()
    )
}

fun PublicQuestionDto.toDomain(): PublicQuestion {
    return PublicQuestion(
        idPregunta = this.idPregunta,
        idCategoriaPregunta = this.idCategoriaPregunta ?: 0L,
        enunciado = this.enunciado,
        nivelDificultad = this.nivelDificultad.orEmpty(),
        tipoPregunta = this.tipoPregunta.orEmpty(),
        puntaje = this.puntaje ?: 1,
        orden = this.orden ?: 0,
        opciones = this.opciones?.map { it.toDomain() } ?: emptyList()
    )
}

fun PublicDiagnosticOptionDto.toDomain(): PublicDiagnosticOption {
    return PublicDiagnosticOption(
        idOpcionDiagnostico = this.idOpcionDiagnostico,
        texto = this.texto,
        orden = this.orden ?: 0
    )
}

fun DiagnosticResultResponseDto.toDomain(): DiagnosticResult {
    return DiagnosticResult(
        idResultado = this.idResultado,
        idUsuario = this.idUsuario,
        idExamen = this.idExamen,
        idNivelAsignado = this.idNivelAsignado,
        nombreNivelAsignado = this.nombreNivelAsignado,
        puntajeObtenido = this.puntajeObtenido ?: 0,
        puntajeMaximo = this.puntajeMaximo ?: 0,
        porcentaje = this.porcentaje ?: 0.0,
        fechaRealizacion = this.fechaRealizacion
    )
}

fun DiagnosticAnswerResponseDto.toDomain(): DiagnosticAnswer {
    return DiagnosticAnswer(
        idRespuesta = this.idRespuesta,
        idResultado = this.idResultado,
        idPregunta = this.idPregunta,
        idOpcionSeleccionada = this.idOpcionSeleccionada,
        esCorrecta = this.esCorrecta
    )
}

fun ExamAnswerSubmission.toDto(): ExamAnswerSubmissionDto {
    return ExamAnswerSubmissionDto(
        idPregunta = this.idPregunta,
        idOpcionSeleccionada = this.idOpcionSeleccionada
    )
}