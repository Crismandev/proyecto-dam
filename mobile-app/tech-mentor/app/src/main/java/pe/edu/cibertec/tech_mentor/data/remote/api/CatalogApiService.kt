package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.CategoryResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.CourseContentResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.CourseResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.LessonResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.LevelResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.ProfessionResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CatalogApiService {

    @GET("catalogo/categorias")
    suspend fun getCategories(): Response<List<CategoryResponseDto>>

    @GET("catalogo/categorias/{id}")
    suspend fun getCategoryById(
        @Path("id") id: Long
    ): Response<CategoryResponseDto>

    @GET("catalogo/cursos")
    suspend fun getCourses(
        @Query("idCategoria") idCategoria: Long? = null
    ): Response<List<CourseResponseDto>>

    @GET("catalogo/cursos/{id}")
    suspend fun getCourseById(
        @Path("id") id: Long
    ): Response<CourseResponseDto>

    @GET("catalogo/cursos/{idCurso}/contenido")
    suspend fun getCourseContent(
        @Path("idCurso") idCurso: Long
    ): Response<CourseContentResponseDto>

    // Profesiones
    @GET("catalogo/profesiones")
    suspend fun getProfessions(): Response<List<ProfessionResponseDto>>

    @GET("catalogo/profesiones/{id}")
    suspend fun getProfessionById(
        @Path("id") id: Long
    ): Response<ProfessionResponseDto>

    // Niveles (Lectura individual)
    @GET("catalogo/niveles")
    suspend fun getLevels(
        @Query("idCurso") idCurso: Long? = null
    ): Response<List<LevelResponseDto>>

    @GET("catalogo/niveles/{id}")
    suspend fun getLevelById(
        @Path("id") id: Long
    ): Response<LevelResponseDto>

    // Lecciones (Lectura individual)
    @GET("catalogo/lecciones")
    suspend fun getLessons(
        @Query("idNivel") idNivel: Long? = null
    ): Response<List<LessonResponseDto>>

    @GET("catalogo/lecciones/{id}")
    suspend fun getLessonById(
        @Path("id") id: Long
    ): Response<LessonResponseDto>
}