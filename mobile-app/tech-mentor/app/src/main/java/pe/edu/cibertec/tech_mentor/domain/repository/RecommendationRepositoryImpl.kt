package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.RecommendationApiService
import pe.edu.cibertec.tech_mentor.domain.model.CategoryAnalysis
import pe.edu.cibertec.tech_mentor.domain.model.CourseRecommendation
import pe.edu.cibertec.tech_mentor.domain.repository.RecommendationRepository

class RecommendationRepositoryImpl(
    private val recommendationApiService: RecommendationApiService
) : RecommendationRepository {

    override suspend fun generateRecommendations(idResultado: Long): ApiResponse<List<CourseRecommendation>> {
        return try {
            val response = recommendationApiService.generateRecommendations(idResultado)
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al generar las recomendaciones (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getMyRecommendations(): ApiResponse<List<CourseRecommendation>> {
        return try {
            val response = recommendationApiService.getMyRecommendations()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener mis recomendaciones (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getRecommendationsByResult(idResultado: Long): ApiResponse<List<CourseRecommendation>> {
        return try {
            val response = recommendationApiService.getRecommendationsByResult(idResultado)
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener las recomendaciones del resultado (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getResultAnalysis(idResultado: Long): ApiResponse<List<CategoryAnalysis>> {
        return try {
            val response = recommendationApiService.getResultAnalysis(idResultado)
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el análisis por categoría (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun changeRecommendationStatus(id: Long, status: String): ApiResponse<CourseRecommendation> {
        return try {
            val response = recommendationApiService.changeRecommendationStatus(id, status)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al actualizar el estado de la recomendación (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }
}