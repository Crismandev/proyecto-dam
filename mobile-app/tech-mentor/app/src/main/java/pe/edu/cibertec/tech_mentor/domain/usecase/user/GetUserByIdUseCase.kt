package pe.edu.cibertec.tech_mentor.domain.usecase.user

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.UserProfile
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository

class GetUserByIdUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(id: Long): ApiResponse<UserProfile> {
        return repository.getUserById(id)
    }
}