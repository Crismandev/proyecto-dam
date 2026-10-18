package pe.edu.cibertec.tech_mentor.domain.usecase.progress

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.LessonProgress
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class GetMyLessonsProgressUseCase(private val repository: ProgressRepository) {
    suspend operator fun invoke(idCurso: Long? = null): ApiResponse<List<LessonProgress>> {
        return repository.getMyLessonsProgress(idCurso)
    }
}