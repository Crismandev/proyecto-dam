package pe.edu.cibertec.tech_mentor.domain.usecase.diagnostic

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.QuestionCategory
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository

class GetQuestionCategoriesUseCase(private val repository: DiagnosticRepository) {
    suspend operator fun invoke(): ApiResponse<List<QuestionCategory>> {
        return repository.getQuestionCategories()
    }
}