package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Category
import pe.edu.cibertec.tech_mentor.domain.model.Course

interface CatalogRepository {
    suspend fun getCategories(): ApiResponse<List<Category>>
    suspend fun getCategoryById(id: Long): ApiResponse<Category>
    suspend fun getCourses(idCategoria: Long? = null): ApiResponse<List<Course>>
    suspend fun getCourseById(id: Long): ApiResponse<Course>
}