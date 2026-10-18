package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.mapper.toDto
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.ProgressApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.progress.CompleteLessonRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.CourseProgress
import pe.edu.cibertec.tech_mentor.domain.model.LessonAnswer
import pe.edu.cibertec.tech_mentor.domain.model.LessonProgress
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class ProgressRepositoryImpl(
    private val progressApiService: ProgressApiService
) : ProgressRepository {

    override suspend fun startCourse(idCurso: Long): ApiResponse<CourseProgress> {
        return try {
            val response = progressApiService.startCourse(idCurso)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al iniciar el curso (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun abandonCourse(idCurso: Long): ApiResponse<CourseProgress> {
        return try {
            val response = progressApiService.abandonCourse(idCurso)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al abandonar el curso (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getMyCoursesProgress(): ApiResponse<List<CourseProgress>> {
        return try {
            val response = progressApiService.getMyCoursesProgress()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener mis cursos (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getMyCourseProgressById(idCurso: Long): ApiResponse<CourseProgress> {
        return try {
            val response = progressApiService.getMyCourseProgressById(idCurso)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el progreso del curso (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun startLesson(idLeccion: Long): ApiResponse<LessonProgress> {
        return try {
            val response = progressApiService.startLesson(idLeccion)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al iniciar la lección (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun completeLesson(
        idLeccion: Long,
        answers: List<LessonAnswer>
    ): ApiResponse<LessonProgress> {
        return try {
            val requestDto = CompleteLessonRequestDto(respuestas = answers.map { it.toDto() })
            val response = progressApiService.completeLesson(idLeccion, requestDto)

            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al completar la lección (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getMyLessonsProgress(idCurso: Long?): ApiResponse<List<LessonProgress>> {
        return try {
            val response = progressApiService.getMyLessonsProgress(idCurso)
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el progreso de las lecciones (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }
}