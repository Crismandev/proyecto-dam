package pe.edu.cibertec.tech_mentor.domain.usecase.progress

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.LessonProgress
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class StartLessonUseCase(private val repository: ProgressRepository) {
    suspend operator fun invoke(idLeccion: Long): ApiResponse<LessonProgress> {
        return repository.startLesson(idLeccion)
    }
}