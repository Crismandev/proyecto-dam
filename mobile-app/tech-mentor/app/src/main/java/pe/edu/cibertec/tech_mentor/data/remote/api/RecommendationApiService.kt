package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.recommendation.CategoryAnalysisResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.recommendation.CourseRecommendationResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RecommendationApiService {
    @POST("recomendaciones/generar/{idResultado}")
    suspend fun generateRecommendations(
        @Path("idResultado") idResultado: Long
    ): Response<List<CourseRecommendationResponseDto>>

    @GET("recomendaciones/mis-recomendaciones")
    suspend fun getMyRecommendations(): Response<List<CourseRecommendationResponseDto>>

    @GET("recomendaciones/resultado/{idResultado}")
    suspend fun getRecommendationsByResult(
        @Path("idResultado") idResultado: Long
    ): Response<List<CourseRecommendationResponseDto>>

    @GET("recomendaciones/resultado/{idResultado}/analisis")
    suspend fun getResultAnalysis(
        @Path("idResultado") idResultado: Long
    ): Response<List<CategoryAnalysisResponseDto>>

    @PATCH("recomendaciones/{id}/estado")
    suspend fun changeRecommendationStatus(
        @Path("id") id: Long,
        @Query("valor") valor: String
    ): Response<CourseRecommendationResponseDto>
}