package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Category
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetCategoriesUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(): ApiResponse<List<Category>> {
        return repository.getCategories()
    }
}