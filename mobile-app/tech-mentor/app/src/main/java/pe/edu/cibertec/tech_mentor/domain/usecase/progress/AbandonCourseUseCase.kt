package pe.edu.cibertec.tech_mentor.domain.usecase.progress

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.CourseProgress
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class AbandonCourseUseCase(private val repository: ProgressRepository) {
    suspend operator fun invoke(idCurso: Long): ApiResponse<CourseProgress> {
        return repository.abandonCourse(idCurso)
    }
}