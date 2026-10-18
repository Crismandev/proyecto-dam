package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.domain.model.Mission
import pe.edu.cibertec.tech_mentor.domain.model.Reward
import pe.edu.cibertec.tech_mentor.domain.model.UserMission
import pe.edu.cibertec.tech_mentor.domain.model.UserReward

interface GamificationRepository {
    suspend fun getMissions(): ApiResponse<List<Mission>>
    suspend fun getMissionById(id: Long): ApiResponse<Mission>
    suspend fun joinMission(idMision: Long): ApiResponse<UserMission>
    suspend fun getMyMissions(): ApiResponse<List<UserMission>>
    suspend fun getRewards(): ApiResponse<List<Reward>>
    suspend fun getRewardById(id: Long): ApiResponse<Reward>
    suspend fun obtainReward(idRecompensa: Long): ApiResponse<UserReward>
    suspend fun getMyRewards(): ApiResponse<List<UserReward>>
    suspend fun claimReward(id: Long): ApiResponse<UserReward>
}