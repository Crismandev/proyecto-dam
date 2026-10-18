package pe.edu.cibertec.tech_mentor.domain.usecase.recommendation

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.CourseRecommendation
import pe.edu.cibertec.tech_mentor.domain.repository.RecommendationRepository

class ChangeRecommendationStatusUseCase(private val repository: RecommendationRepository) {
    suspend operator fun invoke(id: Long, status: String): ApiResponse<CourseRecommendation> {
        return repository.changeRecommendationStatus(id, status)
    }
}