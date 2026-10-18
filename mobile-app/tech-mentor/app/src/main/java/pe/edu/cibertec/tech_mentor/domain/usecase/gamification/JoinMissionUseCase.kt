package pe.edu.cibertec.tech_mentor.domain.usecase.gamification

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.UserMission
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository

class JoinMissionUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(idMision: Long): ApiResponse<UserMission> {
        return repository.joinMission(idMision)
    }
}