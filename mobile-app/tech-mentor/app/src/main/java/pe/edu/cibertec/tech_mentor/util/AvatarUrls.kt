package pe.edu.cibertec.tech_mentor.util

object AvatarUrls {

    fun of(seed: String, existingUrl: String? = null): String {
        if (!existingUrl.isNullOrBlank()) return existingUrl
        return random(seed.ifBlank { "techmentor" })
    }

    fun random(seed: String): String {
        val safe = seed.replace(Regex("[^A-Za-z0-9_-]"), "_").ifBlank { "techmentor" }
        return "https://api.dicebear.com/9.x/adventurer/png?seed=$safe&size=160"
    }
}
