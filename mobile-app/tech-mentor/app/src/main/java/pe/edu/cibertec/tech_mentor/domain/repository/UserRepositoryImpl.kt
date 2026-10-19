package pe.edu.cibertec.tech_mentor.data.repository

import pe.edu.cibertec.tech_mentor.data.mapper.toDomain
import pe.edu.cibertec.tech_mentor.data.mapper.toDto
import pe.edu.cibertec.tech_mentor.data.remote.ApiResponse
import pe.edu.cibertec.tech_mentor.data.remote.api.UserApiService
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.AssignProfessionRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UpdateProfileRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.CreateUserParams
import pe.edu.cibertec.tech_mentor.domain.model.UserProfession
import pe.edu.cibertec.tech_mentor.domain.model.UserProfile
import pe.edu.cibertec.tech_mentor.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userApiService: UserApiService
) : UserRepository {

    override suspend fun getMyProfile(): ApiResponse<UserProfile> {
        return try {
            val response = userApiService.getMyProfile()
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el perfil (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun updateMyProfile(request: UpdateProfileRequestDto): ApiResponse<UserProfile> {
        return try {
            val response = userApiService.updateMyProfile(request)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al actualizar el perfil (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getUserById(id: Long): ApiResponse<UserProfile> {
        return try {
            val response = userApiService.getUserById(id)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al obtener el usuario (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun getMyProfessions(): ApiResponse<List<UserProfession>> {
        return try {
            val response = userApiService.getMyProfessions()
            if (response.isSuccessful && response.body() != null) {
                val list = response.body()!!.map { it.toDomain() }
                ApiResponse.Success(list)
            } else {
                ApiResponse.Error(response.code(), "Error al obtener tus profesiones (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun assignProfession(idProfesion: Long): ApiResponse<UserProfession> {
        return try {
            val response = userApiService.assignProfession(AssignProfessionRequestDto(idProfesion))
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "No se pudo asignar la profesión (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun setPrimaryProfession(idProfesion: Long): ApiResponse<UserProfession> {
        return try {
            val response = userApiService.setPrimaryProfession(idProfesion)
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "No se pudo marcar como profesión principal (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun removeProfession(idProfesion: Long): ApiResponse<Unit> {
        return try {
            val response = userApiService.removeProfession(idProfesion)
            if (response.isSuccessful) {
                ApiResponse.Success(Unit)
            } else {
                ApiResponse.Error(response.code(), "No se pudo quitar la profesión (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }

    override suspend fun createUser(params: CreateUserParams): ApiResponse<UserProfile> {
        return try {
            val response = userApiService.createUser(params.toDto())
            if (response.isSuccessful && response.body() != null) {
                ApiResponse.Success(response.body()!!.toDomain())
            } else {
                ApiResponse.Error(response.code(), "Error al crear el usuario (${response.code()})")
            }
        } catch (e: Exception) {
            ApiResponse.Exception(e)
        }
    }
}