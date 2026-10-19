package pe.edu.cibertec.tech_mentor.domain.repository

import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.CreateUserParams
import pe.edu.cibertec.tech_mentor.domain.model.UserProfession
import pe.edu.cibertec.tech_mentor.domain.model.UserProfile

interface UserRepository {
    suspend fun createUser(params: CreateUserParams): ApiResponse<UserProfile>
    suspend fun getMyProfile(): ApiResponse<UserProfile>
    suspend fun updateMyProfile(request: UpdateProfileRequestDto): ApiResponse<UserProfile>
    suspend fun getUserById(id: Long): ApiResponse<UserProfile>
    suspend fun getMyProfessions(): ApiResponse<List<UserProfession>>
    suspend fun assignProfession(idProfesion: Long): ApiResponse<UserProfession>
    suspend fun setPrimaryProfession(idProfesion: Long): ApiResponse<UserProfession>
    suspend fun removeProfession(idProfesion: Long): ApiResponse<Unit>
}