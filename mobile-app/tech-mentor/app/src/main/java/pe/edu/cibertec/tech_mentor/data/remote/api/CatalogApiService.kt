package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.CategoryResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.CourseContentResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.catalog.CourseResponseDto
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
}