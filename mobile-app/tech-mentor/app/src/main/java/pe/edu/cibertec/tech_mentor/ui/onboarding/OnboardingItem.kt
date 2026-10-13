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

/*
 * Versión anterior conservada por solicitud de la usuaria.
 * La versión activa conserva los textos y la tarjeta, y sustituye imageUrl por los datos visuales de cada ilustración.
 * El código siguiente permanece únicamente como referencia y no se ejecuta.

package pe.edu.cibertec.tech_mentor.ui.onboarding

data class OnboardingItem(
    val imageUrl: String,
    val title: String,
    val description: String,
    val cardIconResId: Int,
    val cardTitle: String,
    val cardSubtitle: String
)
*/
