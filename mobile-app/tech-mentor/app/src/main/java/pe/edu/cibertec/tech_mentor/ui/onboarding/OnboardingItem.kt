package pe.edu.cibertec.tech_mentor.ui.onboarding

data class OnboardingItem(
    val imageUrl: String,
    val title: String,
    val description: String,
    val cardIconResId: Int,
    val cardTitle: String,
    val cardSubtitle: String
)