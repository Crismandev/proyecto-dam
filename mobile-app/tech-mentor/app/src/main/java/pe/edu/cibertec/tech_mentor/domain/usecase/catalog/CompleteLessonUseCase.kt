package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.domain.model.LessonAnswer
import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class CompleteLessonUseCase {
    class CompleteLessonUseCase(private val repository: ProgressRepository) {
        suspend operator fun invoke(idLeccion: Long, answers: List<LessonAnswer>) = repository.completeLesson(idLeccion, answers)
    }
}