package pe.edu.cibertec.tech_mentor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    @SerializedName("correo")
    val email: String,
    @SerializedName("contrasena")
    val password: String
)