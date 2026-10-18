package pe.edu.cibertec.tech_mentor.domain.usecase.gamification

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.UserMission
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository

class GetMyMissionsUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(): ApiResponse<List<UserMission>> {
        return repository.getMyMissions()
    }
}