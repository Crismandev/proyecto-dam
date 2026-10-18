package pe.edu.cibertec.tech_mentor.domain.usecase.diagnostic

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticResult
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository

class GetDiagnosticResultByIdUseCase(private val repository: DiagnosticRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<DiagnosticResult> {
        return repository.getDiagnosticResultById(id)
    }
}