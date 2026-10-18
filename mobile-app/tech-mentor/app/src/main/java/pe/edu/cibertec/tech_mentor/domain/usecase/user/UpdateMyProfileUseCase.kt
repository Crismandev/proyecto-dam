package pe.edu.cibertec.tech_mentor.domain.usecase.user

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.UserProfile
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository

class UpdateMyProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(request: UpdateProfileRequestDto): ApiResponse<UserProfile> {
        return repository.updateMyProfile(request)
    }
}