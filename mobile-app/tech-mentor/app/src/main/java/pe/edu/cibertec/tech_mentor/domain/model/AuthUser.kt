package pe.edu.cibertec.tech_mentor.domain.model

data class AuthUser(
    val idUser: Int,
    val idRole: Int? = null,
    val firstName: String,
    val lastName: String? = null,
    val email: String,
    val avatar: String? = null,
    val totalXp: Int = 0,
    val coins: Int = 0,
    val token: String
)