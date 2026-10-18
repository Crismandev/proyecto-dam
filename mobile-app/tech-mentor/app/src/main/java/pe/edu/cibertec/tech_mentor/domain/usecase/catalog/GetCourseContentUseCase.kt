package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.CourseContent
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetCourseContentUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(idCurso: Long): ApiResponse<CourseContent> {
        return repository.getCourseContent(idCurso)
    }
}