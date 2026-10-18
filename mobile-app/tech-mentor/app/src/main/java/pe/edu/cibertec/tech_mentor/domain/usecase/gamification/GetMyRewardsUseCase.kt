package pe.edu.cibertec.tech_mentor.domain.usecase.gamification

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.UserReward
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository

class GetMyRewardsUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(): ApiResponse<List<UserReward>> {
        return repository.getMyRewards()
    }
}