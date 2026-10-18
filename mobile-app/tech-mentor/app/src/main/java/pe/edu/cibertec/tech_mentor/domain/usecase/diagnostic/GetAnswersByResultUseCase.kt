package pe.edu.cibertec.tech_mentor.domain.usecase.diagnostic

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticAnswer
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository

class GetAnswersByResultUseCase(private val repository: DiagnosticRepository) {
    suspend operator fun invoke(idResultado: Long): ApiResponse<List<DiagnosticAnswer>> {
        return repository.getAnswersByResult(idResultado)
    }
}