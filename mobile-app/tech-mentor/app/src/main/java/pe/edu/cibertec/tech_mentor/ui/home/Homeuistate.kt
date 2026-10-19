package pe.edu.cibertec.tech_mentor.ui.home

import pe.edu.cibertec.tech_mentor.domain.model.Course
import pe.edu.cibertec.tech_mentor.domain.model.CourseProgress
import pe.edu.cibertec.tech_mentor.domain.model.Mission
import pe.edu.cibertec.tech_mentor.domain.model.UserMission
import pe.edu.cibertec.tech_mentor.domain.model.UserProfile

data class HomeUiState(
    val profile: UserProfile? = null,
    val courses: List<Course>? = null,
    val progress: List<CourseProgress>? = null,
    val missions: List<Mission>? = null,
    val userMissions: List<UserMission>? = null
)