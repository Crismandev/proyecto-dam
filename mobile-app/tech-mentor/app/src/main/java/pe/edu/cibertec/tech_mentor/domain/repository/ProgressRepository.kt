package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.CourseProgress
import pe.edu.cibertec.tech_mentor.domain.model.LessonAnswer
import pe.edu.cibertec.tech_mentor.domain.model.LessonProgress

interface ProgressRepository {
    suspend fun startCourse(idCurso: Long): ApiResponse<CourseProgress>
    suspend fun abandonCourse(idCurso: Long): ApiResponse<CourseProgress>
    suspend fun getMyCoursesProgress(): ApiResponse<List<CourseProgress>>
    suspend fun getMyCourseProgressById(idCurso: Long): ApiResponse<CourseProgress>
    suspend fun startLesson(idLeccion: Long): ApiResponse<LessonProgress>
    suspend fun completeLesson(idLeccion: Long, answers: List<LessonAnswer>): ApiResponse<LessonProgress>
    suspend fun getMyLessonsProgress(idCurso: Long? = null): ApiResponse<List<LessonProgress>>
}