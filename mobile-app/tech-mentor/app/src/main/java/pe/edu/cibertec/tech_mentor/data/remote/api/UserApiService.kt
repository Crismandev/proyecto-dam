package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.user.AssignProfessionRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.CreateUserRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UserProfessionResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UserResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserApiService {
    @POST("usuarios")
    suspend fun createUser(
        @Body request: CreateUserRequestDto
    ): Response<UserResponseDto>

    @GET("usuarios/me")
    suspend fun getMyProfile(): Response<UserResponseDto>

    @PUT("usuarios/me")
    suspend fun updateMyProfile(
        @Body request: UpdateProfileRequestDto
    ): Response<UserResponseDto>

    @GET("usuarios/{id}")
    suspend fun getUserById(
        @Path("id") id: Long
    ): Response<UserResponseDto>

    @GET("usuarios/me/profesiones")
    suspend fun getMyProfessions(): Response<List<UserProfessionResponseDto>>

    @POST("usuarios/me/profesiones")
    suspend fun assignProfession(
        @Body request: AssignProfessionRequestDto
    ): Response<UserProfessionResponseDto>

    @PATCH("usuarios/me/profesiones/{idProfesion}/principal")
    suspend fun setPrimaryProfession(
        @Path("idProfesion") idProfesion: Long
    ): Response<UserProfessionResponseDto>

    @DELETE("usuarios/me/profesiones/{idProfesion}")
    suspend fun removeProfession(
        @Path("idProfesion") idProfesion: Long
    ): Response<Unit>
}