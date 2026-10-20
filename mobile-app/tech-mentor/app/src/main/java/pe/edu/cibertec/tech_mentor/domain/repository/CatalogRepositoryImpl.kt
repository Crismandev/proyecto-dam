package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.CatalogApiService
import pe.edu.cibertec.tech_mentor.domain.model.Category
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.model.CourseContent
import pe.edu.cibertec.tech_mentor.domain.model.Profession
import pe.edu.cibertec.tech_mentor.domain.model.Level
import pe.edu.cibertec.tech_mentor.domain.model.Lesson

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

    override suspend fun getLevels(idCurso: Long?): ApiResponse<List<Level>> {
        return try {
            val response = if (idCurso != null && idCurso > 0L) {
                catalogApiService.getLevelsByCourse(idCurso)
            } else {
                catalogApiService.getLevels()
            }
            if (response.isSuccessful && response.body() != null) {
                var list = response.body()!!.map { it.toDomain() }
                if (idCurso != null && idCurso > 0L) {
                    list = list.filter { it.idCurso == idCurso }
                }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener los niveles (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getLevelById(id: Long): ApiResponse<Level> {
        return try {
            val response = catalogApiService.getLevelById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el nivel (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getLessons(idNivel: Long?): ApiResponse<List<Lesson>> {
        return try {
            val response = if (idNivel != null && idNivel > 0L) {
                catalogApiService.getLessonsByLevel(idNivel)
            } else {
                catalogApiService.getLessons()
            }
            if (response.isSuccessful && response.body() != null) {
                var list = response.body()!!.map { it.toDomain() }
                if (idNivel != null && idNivel > 0L) {
                    list = list.filter { it.idNivel == idNivel }
                }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener las lecciones (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getLessonById(id: Long): ApiResponse<Lesson> {
        return try {
            val response = catalogApiService.getLessonById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener la lección (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }
}