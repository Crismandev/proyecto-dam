package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class StartLessonUseCase {
    class StartLessonUseCase(private val repository: ProgressRepository) {
        suspend operator fun invoke(idLeccion: Long) = repository.startLesson(idLeccion)
    }
}