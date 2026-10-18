package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class GetMyCourseProgressByIdUseCase {
    class GetMyCourseProgressByIdUseCase(private val repository: ProgressRepository) {
        suspend operator fun invoke(idCurso: Long) = repository.getMyCourseProgressById(idCurso)
    }
}