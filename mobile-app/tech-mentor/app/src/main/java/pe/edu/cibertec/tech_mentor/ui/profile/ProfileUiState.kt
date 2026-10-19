package pe.edu.cibertec.tech_mentor.ui.profile

import pe.edu.cibertec.tech_mentor.domain.model.UserProfile

data class ProfileUiState(
    val user: UserProfile? = null,
    val profession: String? = null,
    val completedCourses: List<String> = emptyList(),
    val completedRoutes: List<String> = emptyList()
)