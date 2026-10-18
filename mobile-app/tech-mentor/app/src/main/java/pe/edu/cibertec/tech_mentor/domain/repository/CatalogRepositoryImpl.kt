package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.domain.model.Category
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.model.CourseContent
import pe.edu.cibertec.tech_mentor.domain.model.Profession

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

    override suspend fun getCourseContent(idCurso: Long): ApiResponse<CourseContent> {
        return try {
            val response = catalogApiService.getCourseContent(idCurso)
            if (response.isSuccessful && response.body() != null) ApiResponse.Success(response.body()!!.toDomain())
            else ApiResponse.Error(response.code(), "Error fetching course content")
        } catch (e: Exception) { ApiResponse.Exception(e) }
    }

    override suspend fun getProfessions(): ApiResponse<List<Profession>> {
        return try {
            val response = catalogApiService.getProfessions()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener las profesiones (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getProfessionById(id: Long): ApiResponse<Profession> {
        return try {
            val response = catalogApiService.getProfessionById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener la profesión (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }
}