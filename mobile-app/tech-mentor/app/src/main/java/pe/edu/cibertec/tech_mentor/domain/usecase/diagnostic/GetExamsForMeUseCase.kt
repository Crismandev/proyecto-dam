package pe.edu.cibertec.tech_mentor.domain.usecase.diagnostic

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticExam
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository

class GetExamsForMeUseCase(private val repository: DiagnosticRepository) {
    suspend operator fun invoke(): ApiResponse<List<DiagnosticExam>> {
        return repository.getExamsForMe()
    }
}