package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.CambiarClaveDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.LoginRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.LoginResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApiService {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>

    @PUT("auth/cambiar-clave")
    suspend fun cambiarClave(
        @Body request: CambiarClaveDto
    ): Response<Unit>
}