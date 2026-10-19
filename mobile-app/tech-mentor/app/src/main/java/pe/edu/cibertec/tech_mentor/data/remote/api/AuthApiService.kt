package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.ChangePasswordRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.LoginRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {

    // --- Versión anterior conservada: ---
    // @POST("auth/login")
    // suspend fun login(
    //     @Body request: LoginRequestDto
    // ): Response<LoginResponseDto>
    // @PUT("auth/cambiar-clave")
    // suspend fun cambiarClave(
    //     @Body request: ChangePasswordRequestDto
    // ): Response<Unit>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>

    // Endpoint directo opcional en caso de que el backend disponga de auto-registro público
    @POST("auth/registro")
    suspend fun registro(
        @Body request: pe.edu.cibertec.tech_mentor.data.remote.dto.user.CreateUserRequestDto
    ): Response<LoginResponseDto>

    @PUT("auth/cambiar-clave")
    suspend fun cambiarClave(
        @Body request: ChangePasswordRequestDto
    ): Response<Unit>
}