package pe.edu.cibertec.tech_mentor.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private var prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "tech_mentor_session"
        const val USER_TOKEN = "user_token"
        const val USER_ID = "user_id"
        const val USERNAME = "username"
        const val USER_ROLE = "user_role"
        const val ONBOARDING_SEEN = "onboarding_visto"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    // Alias para compatibilidad con código existente
    fun fetchAuthToken(): String? = getToken()

    fun isUserLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    fun saveUserId(idUser: Long) {
        prefs.edit().putLong(USER_ID, idUser).apply()
    }

    fun fetchUserId(): Long {
        return prefs.getLong(USER_ID, -1L)
    }

    fun saveUserDetails(username: String, role: String) {
        prefs.edit()
            .putString(USERNAME, username)
            .putString(USER_ROLE, role)
            .apply()
    }

    fun getUsername(): String? = prefs.getString(USERNAME, null)
    fun getUserRole(): String? = prefs.getString(USER_ROLE, null)

    fun setOnboardingSeen() {
        prefs.edit().putBoolean(ONBOARDING_SEEN, true).apply()
    }

    fun isOnboardingSeen(): Boolean {
        return prefs.getBoolean(ONBOARDING_SEEN, false)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER_ID)
        editor.remove(USERNAME)
        editor.remove(USER_ROLE)
        editor.apply()
    }
}