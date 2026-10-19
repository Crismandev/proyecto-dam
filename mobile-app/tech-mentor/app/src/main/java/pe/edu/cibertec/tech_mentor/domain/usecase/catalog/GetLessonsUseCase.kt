package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Lesson
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetLessonsUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(idNivel: Long? = null): ApiResponse<List<Lesson>> {
        return repository.getLessons(idNivel)
    }
}