package pe.edu.cibertec.tech_mentor.ui.onboarding

data class OnboardingItem(
    val artIconResId: Int,
    val artLabel: String,
    val artSecondaryLabel: String,
    val title: String,
    val description: String,
    val cardIconResId: Int,
    val cardTitle: String,
    val cardSubtitle: String
)