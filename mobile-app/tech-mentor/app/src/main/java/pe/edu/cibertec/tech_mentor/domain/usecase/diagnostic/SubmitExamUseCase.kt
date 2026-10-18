package pe.edu.cibertec.tech_mentor.domain.usecase.diagnostic

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticResult
import pe.edu.cibertec.tech_mentor.domain.model.ExamAnswerSubmission
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository

class SubmitExamUseCase(private val repository: DiagnosticRepository) {
    suspend operator fun invoke(
        idExamen: Long,
        answers: List<ExamAnswerSubmission>
    ): ApiResponse<DiagnosticResult> {
        return repository.submitExam(idExamen, answers)
    }
}