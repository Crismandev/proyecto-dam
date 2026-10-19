package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.user.CreateUserRequestDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UserProfessionResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.user.UserResponseDto
import pe.edu.cibertec.tech_mentor.domain.model.CreateUserParams
import pe.edu.cibertec.tech_mentor.domain.model.UserProfession
import pe.edu.cibertec.tech_mentor.domain.model.UserProfile

fun UserResponseDto.toDomain(): UserProfile {
    return UserProfile(
        idUsuario = this.idUsuario,
        idRol = this.idRol,
        nombreRol = this.nombreRol,
        idEstadoUsuario = this.idEstadoUsuario,
        nombreEstado = this.nombreEstado,
        nombres = this.nombres,
        apellidos = this.apellidos,
        email = this.email,
        emailVerificado = this.emailVerificado,
        fechaNacimiento = this.fechaNacimiento,
        avatarUrl = this.avatarUrl,
        biografia = this.biografia,
        telefono = this.telefono,
        pais = this.pais,
        ciudad = this.ciudad,
        xpTotal = this.xpTotal,
        monedas = this.monedas,
        nivelUsuario = this.nivelUsuario,
        rachaActual = this.rachaActual,
        rachaMaxima = this.rachaMaxima,
        fechaRegistro = this.fechaRegistro
    )
}

fun UserProfessionResponseDto.toDomain(): UserProfession {
    return UserProfession(
        idUsuarioProfesion = this.idUsuarioProfesion,
        idUsuario = this.idUsuario,
        idProfesion = this.idProfesion,
        nombreProfesion = this.nombreProfesion,
        principal = this.principal,
        fechaAsignacion = this.fechaAsignacion
    )
}

fun CreateUserParams.toDto(): CreateUserRequestDto {
    return CreateUserRequestDto(
        idRol = this.idRol,
        idEstadoUsuario = this.idEstadoUsuario,
        nombres = this.nombres,
        apellidos = this.apellidos,
        email = this.email,
        username = this.username,
        password = this.password,
        fechaNacimiento = this.fechaNacimiento,
        telefono = this.telefono,
        pais = this.pais,
        ciudad = this.ciudad
    )
}