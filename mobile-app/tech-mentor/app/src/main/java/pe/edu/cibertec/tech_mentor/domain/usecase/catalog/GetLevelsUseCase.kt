package pe.edu.cibertec.tech_mentor.domain.usecase.catalog

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Level
import pe.edu.cibertec.tech_mentor.domain.repository.CatalogRepository

class GetLevelsUseCase(private val repository: CatalogRepository) {
    suspend operator fun invoke(idCurso: Long? = null): ApiResponse<List<Level>> {
        return repository.getLevels(idCurso)
    }
}