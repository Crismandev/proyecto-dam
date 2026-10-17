package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.AuthResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.RegisterRequestDto
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser
import pe.edu.cibertec.tech_mentor.domain.model.RegisterUserParams

fun AuthResponseDto.toDomain(): AuthUser {
    return AuthUser(
        idUser = this.idUser,
        idRole = this.idRole,
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        avatar = this.avatar,
        totalXp = this.totalXp,
        coins = this.coins,
        token = this.token
    )
}

fun RegisterUserParams.toDto(): RegisterRequestDto {
    return RegisterRequestDto(
        firstName = this.firstName,
        lastName = this.lastName,
        email = this.email,
        username = this.email,
        birthDate = this.birthDate,
        phone = this.phone,
        country = this.country,
        city = this.city,
        biography = this.biography,
        avatar = this.avatar,
        password = this.password
    )
}