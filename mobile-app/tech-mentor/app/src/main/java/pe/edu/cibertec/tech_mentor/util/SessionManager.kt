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

    fun isStaff(): Boolean {
        val role = getUserRole()?.uppercase().orEmpty()
        return role == "ADMINISTRADOR" || role == "MAESTRO" || role == "ADMIN"
    }

    fun setOnboardingSeen() {
        prefs.edit().putBoolean(ONBOARDING_SEEN, true).apply()
    }

    fun isOnboardingSeen(): Boolean {
        return prefs.getBoolean(ONBOARDING_SEEN, false)
    }

    fun saveUserProfile(
        nombres: String?,
        apellidos: String?,
        email: String?,
        telefono: String?,
        pais: String?,
        ciudad: String?,
        fechaNacimiento: String?,
        biografia: String?,
        avatarUrl: String? = null
    ) {
        prefs.edit().apply {
            putString("profile_nombres", nombres)
            putString("profile_apellidos", apellidos)
            putString("profile_email", email)
            putString("profile_telefono", telefono)
            putString("profile_pais", pais)
            putString("profile_ciudad", ciudad)
            putString("profile_fecha_nac", fechaNacimiento)
            putString("profile_biografia", biografia)
            if (avatarUrl != null) putString("profile_avatar", avatarUrl)
            apply()
        }
    }

    fun getProfileNombres(): String? = prefs.getString("profile_nombres", null)
    fun getProfileApellidos(): String? = prefs.getString("profile_apellidos", null)
    fun getProfileEmail(): String? = prefs.getString("profile_email", null)
    fun getProfileTelefono(): String? = prefs.getString("profile_telefono", null)
    fun getProfilePais(): String? = prefs.getString("profile_pais", null)
    fun getProfileCiudad(): String? = prefs.getString("profile_ciudad", null)
    fun getProfileFechaNac(): String? = prefs.getString("profile_fecha_nac", null)
    fun getProfileBiografia(): String? = prefs.getString("profile_biografia", null)
    fun getProfileAvatar(): String? = prefs.getString("profile_avatar", null)

    fun clearSession() {
        val editor = prefs.edit()
        editor.remove(USER_TOKEN)
        editor.remove(USER_ID)
        editor.remove(USERNAME)
        editor.remove(USER_ROLE)
        editor.remove("profile_nombres")
        editor.remove("profile_apellidos")
        editor.remove("profile_email")
        editor.remove("profile_telefono")
        editor.remove("profile_pais")
        editor.remove("profile_ciudad")
        editor.remove("profile_fecha_nac")
        editor.remove("profile_biografia")
        editor.remove("profile_avatar")
        editor.apply()
    }
}