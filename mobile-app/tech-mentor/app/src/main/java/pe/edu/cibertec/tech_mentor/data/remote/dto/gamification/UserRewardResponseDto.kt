package pe.edu.cibertec.tech_mentor.data.remote.dto.gamification

data class UserRewardResponseDto(
    val idRecompensaUsuario: Long,
    val idUsuario: Long,
    val idRecompensa: Long,
    val nombreRecompensa: String?,
    val tipo: String?,
    val cantidad: Int?,
    val fechaObtencion: String?,
    val reclamada: Boolean?
)