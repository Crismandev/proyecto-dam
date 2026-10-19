package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic.DiagnosticAnswerResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic.DiagnosticExamResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic.DiagnosticResultResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic.ExamToTakeResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic.QuestionCategoryResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic.SubmitExamRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DiagnosticApiService {
    // Exámenes
    @GET("diagnostico/examenes/para-mi")
    suspend fun getExamsForMe(): Response<List<DiagnosticExamResponseDto>>

    @GET("diagnostico/examenes/{id}")
    suspend fun getExamById(
        @Path("id") id: Long
    ): Response<DiagnosticExamResponseDto>

    @GET("diagnostico/examenes/{id}/rendir")
    suspend fun getExamToTake(
        @Path("id") id: Long
    ): Response<ExamToTakeResponseDto>

    // Rendir examen y Resultados
    @POST("diagnostico/examenes/{idExamen}/rendir")
    suspend fun submitExam(
        @Path("idExamen") idExamen: Long,
        @Body request: SubmitExamRequestDto
    ): Response<DiagnosticResultResponseDto>

    @GET("diagnostico/resultados/mis-resultados")
    suspend fun getMyDiagnosticResults(): Response<List<DiagnosticResultResponseDto>>

    @GET("diagnostico/resultados/{id}")
    suspend fun getDiagnosticResultById(
        @Path("id") id: Long
    ): Response<DiagnosticResultResponseDto>

    // Respuestas de un resultado
    @GET("diagnostico/resultados/{idResultado}/respuestas")
    suspend fun getAnswersByResult(
        @Path("idResultado") idResultado: Long
    ): Response<List<DiagnosticAnswerResponseDto>>

    // Categorías de pregunta
    @GET("diagnostico/categorias-pregunta")
    suspend fun getQuestionCategories(): Response<List<QuestionCategoryResponseDto>>

    @GET("diagnostico/categorias-pregunta/{id}")
    suspend fun getQuestionCategoryById(
        @Path("id") id: Long
    ): Response<QuestionCategoryResponseDto>
}