package pe.edu.cibertec.tech_mentor.domain.usecase.auth

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository

class ChangePasswordUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(claveActual: String, nuevaClave: String): ApiResponse<Unit> {
        return repository.cambiarClave(claveActual, nuevaClave)
    }
}