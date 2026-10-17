package pe.edu.cibertec.tech_mentor.data.local

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "tech_mentor_prefs"
        private const val USER_TOKEN = "user_token"
        private const val USER_ID = "user_id"
        private const val ONBOARDING_SEEN = "onboarding_visto"
    }

    fun saveAuthToken(token: String) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserId(idUser: Int) {
        prefs.edit().putInt(USER_ID, idUser).apply()
    }

    fun fetchUserId(): Int {
        return prefs.getInt(USER_ID, -1)
    }

    fun isUserLoggedIn(): Boolean {
        return !fetchAuthToken().isNullOrEmpty()
    }

    fun clearSession() {
        prefs.edit()
            .remove(USER_TOKEN)
            .remove(USER_ID)
            .apply()
    }

    fun setOnboardingSeen() {
        prefs.edit().putBoolean(ONBOARDING_SEEN, true).apply()
    }

    fun isOnboardingSeen(): Boolean {
        return prefs.getBoolean(ONBOARDING_SEEN, false)
    }
}