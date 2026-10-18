package pe.edu.cibertec.tech_mentor.domain.model

data class UserReward(
    val idRecompensaUsuario: Long,
    val idUsuario: Long,
    val idRecompensa: Long,
    val nombreRecompensa: String,
    val tipo: String,
    val cantidad: Int,
    val fechaObtencion: String?,
    val reclamada: Boolean
)