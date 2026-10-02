package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.LoginRequest
import pe.edu.cibertec.tech_mentor.data.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("usuarios/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}