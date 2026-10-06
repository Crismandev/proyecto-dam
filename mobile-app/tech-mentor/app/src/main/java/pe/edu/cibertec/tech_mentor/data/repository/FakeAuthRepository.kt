package pe.edu.cibertec.tech_mentor.data.repository

import kotlinx.coroutines.delay
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository

class FakeAuthRepository(private val sessionManager: SessionManager) : AuthRepository {

    override suspend fun login(correo: String, contrasena: String): Result<String> {
        delay(2000)

        return if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
            val fakeToken = "jwt_token_falso_para_pruebas_12345"
            sessionManager.saveAuthToken(fakeToken)
            Result.success("Login exitoso. Token: $fakeToken")
        } else {
            Result.failure(Exception("Credenciales incorrectas"))
        }
    }

    override fun logout() {
        sessionManager.clearSession()
    }

    override fun isUserLoggedIn(): Boolean {
        return sessionManager.isUserLoggedIn()
    }
}