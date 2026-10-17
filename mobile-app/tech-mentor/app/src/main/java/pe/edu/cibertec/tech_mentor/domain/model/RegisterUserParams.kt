package pe.edu.cibertec.tech_mentor.domain.model

data class RegisterUserParams(
    val firstName: String,
    val lastName: String,
    val email: String,
    val birthDate: String,
    val phone: String,
    val country: String,
    val city: String,
    val biography: String,
    val avatar: String? = null,
    val password: String
)