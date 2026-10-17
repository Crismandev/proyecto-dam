package pe.edu.cibertec.tech_mentor.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponseDto(
    @SerializedName("token")
    val token: String,
    @SerializedName("idUsuario")
    val idUser: Int,
    @SerializedName("idRol")
    val idRole: Int? = null,
    @SerializedName("nombres")
    val firstName: String,
    @SerializedName("apellidos")
    val lastName: String? = null,
    @SerializedName("correo")
    val email: String,
    @SerializedName("avatar")
    val avatar: String? = null,
    @SerializedName("xpTotal")
    val totalXp: Int = 0,
    @SerializedName("monedas")
    val coins: Int = 0
)