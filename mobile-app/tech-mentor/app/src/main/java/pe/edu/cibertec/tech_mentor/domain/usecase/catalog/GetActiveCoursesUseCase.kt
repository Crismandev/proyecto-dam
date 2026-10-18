package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetActiveCoursesUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(idCategoria: Long? = null): ApiResponse<List<Course>> {
        return repository.getActiveCourses(idCategoria)
    }
}