package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Category
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetCategoryByIdUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<Category> {
        return repository.getCategoryById(id)
    }
}