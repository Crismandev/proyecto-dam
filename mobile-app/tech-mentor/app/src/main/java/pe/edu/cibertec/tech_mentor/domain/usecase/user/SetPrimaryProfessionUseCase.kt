package pe.edu.cibertec.tech_mentor.domain.usecase.user

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.UserProfession
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository

class SetPrimaryProfessionUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(idProfesion: Long): ApiResponse<UserProfession> {
        return repository.setPrimaryProfession(idProfesion)
    }
}