package pe.edu.cibertec.tech_mentor.domain.usecase.user

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.CreateUserParams
import pe.edu.cibertec.tech_mentor.domain.model.UserProfile
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository

class CreateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(params: CreateUserParams): ApiResponse<UserProfile> {
        return repository.createUser(params)
    }
}