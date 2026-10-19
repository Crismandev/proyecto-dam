package pe.edu.cibertec.tech_mentor.domain.usecase.diagnostic

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.QuestionCategory
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository

class GetQuestionCategoryByIdUseCase(private val repository: DiagnosticRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<QuestionCategory> {
        return repository.getQuestionCategoryById(id)
    }
}