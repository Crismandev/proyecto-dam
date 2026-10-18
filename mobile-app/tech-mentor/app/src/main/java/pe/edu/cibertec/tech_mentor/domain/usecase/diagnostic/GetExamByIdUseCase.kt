package pe.edu.cibertec.tech_mentor.domain.usecase.diagnostic

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticExam
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository

class GetExamByIdUseCase(private val repository: DiagnosticRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<DiagnosticExam> {
        return repository.getExamById(id)
    }
}