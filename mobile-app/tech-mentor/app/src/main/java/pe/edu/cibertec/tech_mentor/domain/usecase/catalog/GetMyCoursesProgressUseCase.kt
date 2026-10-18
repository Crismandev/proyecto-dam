package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.domain.repository.ProgressRepository

class GetMyCoursesProgressUseCase {
    class GetMyCoursesProgressUseCase(private val repository: ProgressRepository) {
        suspend operator fun invoke() = repository.getMyCoursesProgress()
    }
}