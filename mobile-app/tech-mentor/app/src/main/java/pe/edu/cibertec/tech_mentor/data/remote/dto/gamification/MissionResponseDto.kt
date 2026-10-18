package pe.edu.cibertec.tech_mentor.data.remote.dto.gamification

data class MissionResponseDto(
    val idMision: Long,
    val nombre: String,
    val descripcion: String?,
    val tipo: String,
    val meta: Int?,
    val xpRecompensa: Int?,
    val fechaInicio: String?,
    val fechaFin: String?,
    val activa: Boolean?
)