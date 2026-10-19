package pe.edu.cibertec.tech_mentor.util

/**
 * URL del backend. No se edita aquí.
 *
 * Cada integrante pone `api.ip` en `local.properties` (no se sube a git).
 * Gradle genera BuildConfig.API_BASE_URL al hacer Sync/Rebuild.
 * Si BuildConfig aún no existe (Sync pendiente), se usa el emulador.
 */
object ApiConfig {

    private const val EMULATOR_URL = "http://10.0.2.2:8080/"

    fun baseUrl(): String {
        val fromBuild = runCatching {
            Class.forName("pe.edu.cibertec.tech_mentor.BuildConfig")
                .getField("API_BASE_URL")
                .get(null) as? String
        }.getOrNull()
        val url = fromBuild?.trim().orEmpty().ifEmpty { EMULATOR_URL }
        return if (url.endsWith("/")) url else "$url/"
    }
}
