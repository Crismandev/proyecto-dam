package pe.edu.cibertec.tech_mentor.domain.usecase.gamification

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Mission
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository

class GetMissionByIdUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<Mission> {
        return repository.getMissionById(id)
    }
}