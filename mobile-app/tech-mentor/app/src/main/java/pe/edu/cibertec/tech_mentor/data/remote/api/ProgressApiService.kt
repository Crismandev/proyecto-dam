package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.progress.CompleteLessonRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.progress.CourseProgressResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.progress.LessonProgressResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProgressApiService {
    // Progreso de Curso
    @POST("progreso/cursos/{idCurso}/iniciar")
    suspend fun startCourse(
        @Path("idCurso") idCurso: Long
    ): Response<CourseProgressResponseDto>

    @PUT("progreso/cursos/{idCurso}/abandonar")
    suspend fun abandonCourse(
        @Path("idCurso") idCurso: Long
    ): Response<CourseProgressResponseDto>

    @GET("progreso/cursos/mis-cursos")
    suspend fun getMyCoursesProgress(): Response<List<CourseProgressResponseDto>>

    @GET("progreso/cursos/mis-cursos/{idCurso}")
    suspend fun getMyCourseProgressById(
        @Path("idCurso") idCurso: Long
    ): Response<CourseProgressResponseDto>

    // Progreso de Lección
    @POST("progreso/lecciones/{idLeccion}/iniciar")
    suspend fun startLesson(
        @Path("idLeccion") idLeccion: Long
    ): Response<LessonProgressResponseDto>

    @POST("progreso/lecciones/{idLeccion}/completar")
    suspend fun completeLesson(
        @Path("idLeccion") idLeccion: Long,
        @Body request: CompleteLessonRequestDto
    ): Response<LessonProgressResponseDto>

    @GET("progreso/lecciones/mis-lecciones")
    suspend fun getMyLessonsProgress(
        @Query("idCurso") idCurso: Long? = null
    ): Response<List<LessonProgressResponseDto>>
}