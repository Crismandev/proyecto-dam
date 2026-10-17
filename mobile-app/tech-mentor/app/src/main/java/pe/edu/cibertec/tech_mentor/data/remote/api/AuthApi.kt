package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.AuthResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.LoginRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.RegisterRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<AuthResponseDto>

    @POST("auth/registro")
    suspend fun register(@Body request: RegisterRequestDto): Response<AuthResponseDto>
}