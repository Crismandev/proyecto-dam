package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.CategoryAnalysis
import pe.edu.cibertec.tech_mentor.domain.model.CourseRecommendation

interface RecommendationRepository {
    suspend fun generateRecommendations(idResultado: Long): ApiResponse<List<CourseRecommendation>>
    suspend fun getMyRecommendations(): ApiResponse<List<CourseRecommendation>>
    suspend fun getRecommendationsByResult(idResultado: Long): ApiResponse<List<CourseRecommendation>>
    suspend fun getResultAnalysis(idResultado: Long): ApiResponse<List<CategoryAnalysis>>
    suspend fun changeRecommendationStatus(id: Long, status: String): ApiResponse<CourseRecommendation>
}