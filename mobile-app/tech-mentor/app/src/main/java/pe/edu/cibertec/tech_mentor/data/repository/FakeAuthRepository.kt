package pe.edu.cibertec.tech_mentor.data.repository

import kotlinx.coroutines.delay
import pe.edu.cibertec.tech_mentor.data.local.SessionManager
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser
import pe.edu.cibertec.tech_mentor.domain.model.RegisterUserParams
import pe.edu.cibertec.tech_mentor.domain.repository.AuthRepository

class FakeAuthRepository(
    private val sessionManager: SessionManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthUser> {
        delay(1500)

        return if (email.isNotBlank() && password.isNotBlank()) {
            val fakeToken = "fake_jwt_token_12345"
            val fakeUser = AuthUser(
                idUser = 1,
                idRole = 2,
                firstName = "Luis",
                lastName = "Demo",
                email = email,
                avatar = null,
                totalXp = 120,
                coins = 50,
                token = fakeToken
            )
            sessionManager.saveAuthToken(fakeUser.token)
            sessionManager.saveUserId(fakeUser.idUser)
            sessionManager.setOnboardingSeen()
            Result.success(fakeUser)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }

    override suspend fun register(params: RegisterUserParams): Result<AuthUser> {
        delay(1500)

        return if (params.firstName.isNotBlank() && params.email.isNotBlank() && params.password.isNotBlank()) {
            val fakeToken = "fake_jwt_token_register_67890"
            val newUser = AuthUser(
                idUser = 2,
                idRole = 2,
                firstName = params.firstName,
                lastName = params.lastName,
                email = params.email,
                avatar = params.avatar,
                totalXp = 0,
                coins = 0,
                token = fakeToken
            )
            sessionManager.saveAuthToken(newUser.token)
            sessionManager.saveUserId(newUser.idUser)
            sessionManager.setOnboardingSeen()
            Result.success(newUser)
        } else {
            Result.failure(Exception("Please complete the required fields"))
        }
    }

    override fun logout() {
        sessionManager.clearSession()
    }

    override fun isUserLoggedIn(): Boolean {
        return sessionManager.isUserLoggedIn()
    }
}