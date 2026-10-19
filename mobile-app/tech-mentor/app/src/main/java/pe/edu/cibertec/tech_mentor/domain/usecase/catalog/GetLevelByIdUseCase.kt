package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Level
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetLevelByIdUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<Level> {
        return repository.getLevelById(id)
    }
}