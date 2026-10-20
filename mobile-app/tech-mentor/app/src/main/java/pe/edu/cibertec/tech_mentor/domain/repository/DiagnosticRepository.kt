package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticAnswer
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticExam
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticResult
import pe.edu.cibertec.tech_mentor.domain.model.ExamAnswerSubmission
import pe.edu.cibertec.tech_mentor.domain.model.ExamToTake
import pe.edu.cibertec.tech_mentor.domain.model.QuestionCategory

interface DiagnosticRepository {
    suspend fun getExams(): ApiResponse<List<DiagnosticExam>>
    suspend fun getExamsForMe(): ApiResponse<List<DiagnosticExam>>
    suspend fun getExamById(id: Long): ApiResponse<DiagnosticExam>
    suspend fun getExamToTake(id: Long): ApiResponse<ExamToTake>
    suspend fun submitExam(idExamen: Long, answers: List<ExamAnswerSubmission>): ApiResponse<DiagnosticResult>
    suspend fun getMyDiagnosticResults(): ApiResponse<List<DiagnosticResult>>
    suspend fun getDiagnosticResultById(id: Long): ApiResponse<DiagnosticResult>
    suspend fun getAnswersByResult(idResultado: Long): ApiResponse<List<DiagnosticAnswer>>
    suspend fun getQuestionCategories(): ApiResponse<List<QuestionCategory>>
    suspend fun getQuestionCategoryById(id: Long): ApiResponse<QuestionCategory>
}