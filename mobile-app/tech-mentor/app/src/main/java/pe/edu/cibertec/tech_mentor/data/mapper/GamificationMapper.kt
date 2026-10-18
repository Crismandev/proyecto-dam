package pe.edu.cibertec.tech_mentor.data.mapper

import pe.edu.cibertec.tech_mentor.data.remote.dto.gamification.MissionResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.gamification.RewardResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.gamification.UserMissionResponseDto
import pe.edu.cibertec.tech_mentor.data.remote.dto.gamification.UserRewardResponseDto
import pe.edu.cibertec.tech_mentor.domain.model.Mission
import pe.edu.cibertec.tech_mentor.domain.model.Reward
import pe.edu.cibertec.tech_mentor.domain.model.UserMission
import pe.edu.cibertec.tech_mentor.domain.model.UserReward

fun MissionResponseDto.toDomain(): Mission {
    return Mission(
        idMision = this.idMision,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        tipo = this.tipo,
        meta = this.meta ?: 1,
        xpRecompensa = this.xpRecompensa ?: 0,
        fechaInicio = this.fechaInicio,
        fechaFin = this.fechaFin,
        activa = this.activa ?: true
    )
}

fun UserMissionResponseDto.toDomain(): UserMission {
    return UserMission(
        idMisionUsuario = this.idMisionUsuario,
        idUsuario = this.idUsuario,
        idMision = this.idMision,
        nombreMision = this.nombreMision.orEmpty(),
        meta = this.meta ?: 1,
        progresoActual = this.progresoActual ?: 0,
        completada = this.completada ?: false,
        fechaAsignacion = this.fechaAsignacion,
        fechaCompletada = this.fechaCompletada
    )
}

fun RewardResponseDto.toDomain(): Reward {
    return Reward(
        idRecompensa = this.idRecompensa,
        nombre = this.nombre,
        descripcion = this.descripcion.orEmpty(),
        tipo = this.tipo,
        cantidad = this.cantidad ?: 0,
        costoMonedas = this.costoMonedas ?: 0,
        diaSecuenciaRequerido = this.diaSecuenciaRequerido,
        activa = this.activa ?: true
    )
}

fun UserRewardResponseDto.toDomain(): UserReward {
    return UserReward(
        idRecompensaUsuario = this.idRecompensaUsuario,
        idUsuario = this.idUsuario,
        idRecompensa = this.idRecompensa,
        nombreRecompensa = this.nombreRecompensa.orEmpty(),
        tipo = this.tipo.orEmpty(),
        cantidad = this.cantidad ?: 0,
        fechaObtencion = this.fechaObtencion,
        reclamada = this.reclamada ?: false
    )
}