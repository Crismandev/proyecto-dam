package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class GetMyLessonsProgressUseCase {
    class GetMyLessonsProgressUseCase(private val repository: ProgressRepository) {
        suspend operator fun invoke(idCurso: Long? = null) = repository.getMyLessonsProgress(idCurso)
    }
}