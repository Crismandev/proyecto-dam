package pe.edu.cibertec.tech_mentor.domain.usecase.gamification

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.UserReward
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository

class ClaimRewardUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<UserReward> {
        return repository.claimReward(id)
    }
}