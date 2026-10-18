package pe.edu.cibertec.tech_mentor.domain.usecase.progress

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.LessonAnswer
import pe.edu.cibertec.tech_mentor.domain.model.LessonProgress
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class CompleteLessonUseCase(private val repository: ProgressRepository) {
    suspend operator fun invoke(
        idLeccion: Long,
        answers: List<LessonAnswer>
    ): ApiResponse<LessonProgress> {
        return repository.completeLesson(idLeccion, answers)
    }
}