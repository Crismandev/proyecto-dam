package pe.edu.cibertec.tech_mentor.domain.usecase.auth

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository

class LoginUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String): ApiResponse<AuthUser> {
        return repository.login(username, password)
    }
}