package pe.edu.cibertec.tech_mentor.domain.usecase.diagnostic

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.ExamToTake
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository

class GetExamToTakeUseCase(private val repository: DiagnosticRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<ExamToTake> {
        return repository.getExamToTake(id)
    }
}