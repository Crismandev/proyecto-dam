package pe.edu.cibertec.tech_mentor.domain.usecase.user

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository

class RemoveProfessionUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(idProfesion: Long): ApiResponse<Unit> {
        return repository.removeProfession(idProfesion)
    }
}