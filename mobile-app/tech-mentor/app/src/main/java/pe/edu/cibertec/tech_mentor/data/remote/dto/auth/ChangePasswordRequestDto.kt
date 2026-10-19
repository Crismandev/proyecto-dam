package pe.edu.cibertec.tech_mentor.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

// --- Versión anterior (comentada): ---
// data class ChangePasswordRequestDto(
//     val claveActual: String,
//     val nuevaClave: String
// )

// Adaptado con @SerializedName("claveNueva") para coincidir con CambiarClaveDto en Spring Boot:
data class ChangePasswordRequestDto(
    val claveActual: String,
    @SerializedName("claveNueva")
    val nuevaClave: String
)