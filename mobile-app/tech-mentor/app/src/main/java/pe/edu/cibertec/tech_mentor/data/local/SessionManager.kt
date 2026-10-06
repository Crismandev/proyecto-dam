package pe.edu.cibertec.tech_mentor.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREF_NAME = "tech_mentor_prefs"
        const val USER_TOKEN = "user_token"
        const val ONBOARDING_VISTO = "onboarding_visto"
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearSession() {
        prefs.edit().remove(USER_TOKEN).apply()
    }

    fun isUserLoggedIn(): Boolean {
        return !fetchAuthToken().isNullOrEmpty()
    }

    fun setOnboardingVisto() {
        prefs.edit().putBoolean(ONBOARDING_VISTO, true).apply()
    }

    fun isOnboardingVisto(): Boolean {
        return prefs.getBoolean(ONBOARDING_VISTO, false)
    }
}