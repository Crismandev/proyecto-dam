package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class AbandonCourseUseCase {
    class AbandonCourseUseCase(private val repository: ProgressRepository) {
        suspend operator fun invoke(idCurso: Long) = repository.abandonCourse(idCurso)
    }
}