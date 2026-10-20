package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.mapper.toDto
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.DiagnosticApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.diagnostic.SubmitExamRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticAnswer
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticExam
import pe.edu.cibertec.tech_mentor.domain.model.DiagnosticResult
import pe.edu.cibertec.tech_mentor.domain.model.ExamAnswerSubmission
import pe.edu.cibertec.tech_mentor.domain.model.ExamToTake
import pe.edu.cibertec.tech_mentor.domain.repository.DiagnosticRepository
import pe.edu.cibertec.tech_mentor.domain.model.QuestionCategory

class DiagnosticRepositoryImpl(
    private val diagnosticApiService: DiagnosticApiService
) : DiagnosticRepository {

    override suspend fun getExams(): ApiResponse<List<DiagnosticExam>> {
        return try {
            val response = diagnosticApiService.getExams()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener los exámenes (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getExamsForMe(): ApiResponse<List<DiagnosticExam>> {
        return try {
            val response = diagnosticApiService.getExamsForMe()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener los exámenes recomendados (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getExamById(id: Long): ApiResponse<DiagnosticExam> {
        return try {
            val response = diagnosticApiService.getExamById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el examen (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getExamToTake(id: Long): ApiResponse<ExamToTake> {
        return try {
            val response = diagnosticApiService.getExamToTake(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al cargar las preguntas del examen (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun submitExam(
        idExamen: Long,
        answers: List<ExamAnswerSubmission>
    ): ApiResponse<DiagnosticResult> {
        return try {
            val requestDto = SubmitExamRequestDto(respuestas = answers.map { it.toDto() })
            val response = diagnosticApiService.submitExam(idExamen, requestDto)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al enviar las respuestas del examen (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getMyDiagnosticResults(): ApiResponse<List<DiagnosticResult>> {
        return try {
            val response = diagnosticApiService.getMyDiagnosticResults()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el historial de resultados (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getDiagnosticResultById(id: Long): ApiResponse<DiagnosticResult> {
        return try {
            val response = diagnosticApiService.getDiagnosticResultById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el detalle del resultado (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getAnswersByResult(idResultado: Long): ApiResponse<List<DiagnosticAnswer>> {
        return try {
            val response = diagnosticApiService.getAnswersByResult(idResultado)
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener las respuestas del examen (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getQuestionCategories(): ApiResponse<List<QuestionCategory>> {
        return try {
            val response = diagnosticApiService.getQuestionCategories()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener las categorías de pregunta (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getQuestionCategoryById(id: Long): ApiResponse<QuestionCategory> {
        return try {
            val response = diagnosticApiService.getQuestionCategoryById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener la categoría de pregunta (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }
}