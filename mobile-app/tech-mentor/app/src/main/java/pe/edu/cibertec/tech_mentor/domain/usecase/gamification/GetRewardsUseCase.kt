package pe.edu.cibertec.tech_mentor.domain.usecase.gamification

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Reward
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository

class GetRewardsUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(): ApiResponse<List<Reward>> {
        return repository.getRewards()
    }
}