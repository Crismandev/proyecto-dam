package pe.edu.cibertec.tech_mentor.data.remote.api

import pe.edu.cibertec.tech_mentor.data.remote.dto.gamification.MissionResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.gamification.RewardResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.gamification.UserMissionResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.gamification.UserRewardResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface GamificationApiService {

    // Catálogo de Misiones
    @GET("gamificacion/misiones")
    suspend fun getMissions(): Response<List<MissionResponseDto>>

    @GET("gamificacion/misiones/{id}")
    suspend fun getMissionById(
        @Path("id") id: Long
    ): Response<MissionResponseDto>

    // Misiones del Usuario
    @POST("gamificacion/misiones/{idMision}/unirse")
    suspend fun joinMission(
        @Path("idMision") idMision: Long
    ): Response<UserMissionResponseDto>

    @GET("gamificacion/mis-misiones")
    suspend fun getMyMissions(): Response<List<UserMissionResponseDto>>

    // Catálogo de Recompensas
    @GET("gamificacion/recompensas")
    suspend fun getRewards(): Response<List<RewardResponseDto>>

    @GET("gamificacion/recompensas/{id}")
    suspend fun getRewardById(
        @Path("id") id: Long
    ): Response<RewardResponseDto>

    // Recompensas del Usuario
    @POST("gamificacion/recompensas/{idRecompensa}/obtener")
    suspend fun obtainReward(
        @Path("idRecompensa") idRecompensa: Long
    ): Response<UserRewardResponseDto>

    @GET("gamificacion/mis-recompensas")
    suspend fun getMyRewards(): Response<List<UserRewardResponseDto>>

    @PATCH("gamificacion/mis-recompensas/{id}/reclamar")
    suspend fun claimReward(
        @Path("id") id: Long
    ): Response<UserRewardResponseDto>
}