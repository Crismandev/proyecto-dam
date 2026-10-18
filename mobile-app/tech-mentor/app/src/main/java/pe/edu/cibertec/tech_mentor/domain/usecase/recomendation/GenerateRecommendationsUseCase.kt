package pe.edu.cibertec.tech_mentor.domain.usecase.recommendation

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.CourseRecommendation
import pe.edu.cibertec.tech_mentor.domain.repository.RecommendationRepository

class GenerateRecommendationsUseCase(private val repository: RecommendationRepository) {
    suspend operator fun invoke(idResultado: Long): ApiResponse<List<CourseRecommendation>> {
        return repository.generateRecommendations(idResultado)
    }
}