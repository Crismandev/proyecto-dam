package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.recommendation.CategoryAnalysisResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.recommendation.CourseRecommendationResponseDto
import pe.edu.cibertec.tech_mentor.domain.model.CategoryAnalysis
import pe.edu.cibertec.tech_mentor.domain.model.CourseRecommendation

fun CourseRecommendationResponseDto.toDomain(): CourseRecommendation {
    return CourseRecommendation(
        idRecomendacion = this.idRecomendacion,
        idUsuario = this.idUsuario,
        idResultado = this.idResultado,
        idCurso = this.idCurso,
        nombreCurso = this.nombreCurso.orEmpty(),
        idProfesion = this.idProfesion,
        nombreProfesion = this.nombreProfesion,
        prioridad = this.prioridad ?: 3,
        motivo = this.motivo.orEmpty(),
        origen = this.origen.orEmpty(),
        explicacionIa = this.explicacionIa,
        estado = this.estado ?: "PENDIENTE",
        fechaCreacion = this.fechaCreacion
    )
}

fun CategoryAnalysisResponseDto.toDomain(): CategoryAnalysis {
    return CategoryAnalysis(
        idCategoriaPregunta = this.idCategoriaPregunta,
        categoria = this.categoria.orEmpty(),
        puntajeObtenido = this.puntajeObtenido ?: 0,
        puntajeMaximo = this.puntajeMaximo ?: 0,
        porcentaje = this.porcentaje ?: 0.0
    )
}