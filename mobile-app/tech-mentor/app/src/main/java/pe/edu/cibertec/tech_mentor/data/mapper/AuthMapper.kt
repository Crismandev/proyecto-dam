package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.auth.LoginResponseDto
import pe.edu.cibertec.tech_mentor.domain.model.AuthUser

fun LoginResponseDto.toDomain(): AuthUser {
    return AuthUser(
        idUsuario = this.idUsuario,
        username = this.username,
        rol = this.rol,
        token = this.token
    )
}