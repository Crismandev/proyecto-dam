package pe.edu.cibertec.tech_mentor.domain.usecase.gamification

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Mission
import pe.edu.cibertec.tech_mentor.domain.repository.GamificationRepository

class GetMissionsUseCase(private val repository: GamificationRepository) {
    suspend operator fun invoke(): ApiResponse<List<Mission>> {
        return repository.getMissions()
    }
}