package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.domain.model.Category
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class CatalogRepositoryImpl(
    private val catalogApiService: CatalogApiService
) : CatalogRepository {

    override suspend fun getCategories(): ApiResponse<List<Category>> {
        return try {
            val response = catalogApiService.getCategories()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener las categorías (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getCategoryById(id: Long): ApiResponse<Category> {
        return try {
            val response = catalogApiService.getCategoryById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener la categoría (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getCourses(idCategoria: Long?): ApiResponse<List<Course>> {
        return try {
            val response = catalogApiService.getCourses(idCategoria)
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener los cursos (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getCourseById(id: Long): ApiResponse<Course> {
        return try {
            val response = catalogApiService.getCourseById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el curso (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }
}