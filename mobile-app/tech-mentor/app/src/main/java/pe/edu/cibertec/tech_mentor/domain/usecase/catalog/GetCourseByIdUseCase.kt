package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetCourseByIdUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<Course> {
        return repository.getCourseById(id)
    }
}