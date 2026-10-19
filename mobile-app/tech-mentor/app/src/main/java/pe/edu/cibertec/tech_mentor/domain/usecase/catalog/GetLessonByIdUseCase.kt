package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Lesson
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetLessonByIdUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<Lesson> {
        return repository.getLessonById(id)
    }
}