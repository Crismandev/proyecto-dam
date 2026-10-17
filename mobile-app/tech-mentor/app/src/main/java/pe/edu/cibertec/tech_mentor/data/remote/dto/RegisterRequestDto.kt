package pe.edu.cibertec.tech_mentor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequestDto(
    @SerializedName("nombres")
    val firstName: String,
    @SerializedName("apellidos")
    val lastName: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("fechaNacimiento")
    val birthDate: String,
    @SerializedName("telefono")
    val phone: String,
    @SerializedName("pais")
    val country: String,
    @SerializedName("ciudad")
    val city: String,
    @SerializedName("biografia")
    val biography: String,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("contrasena")
    val password: String
)