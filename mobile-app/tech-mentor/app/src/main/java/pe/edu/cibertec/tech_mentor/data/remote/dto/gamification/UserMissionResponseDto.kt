package pe.edu.cibertec.tech_mentor.data.remote.dto.gamification

data class UserMissionResponseDto(
    val idMisionUsuario: Long,
    val idUsuario: Long,
    val idMision: Long,
    val nombreMision: String?,
    val meta: Int?,
    val progresoActual: Int?,
    val completada: Boolean?,
    val fechaAsignacion: String?,
    val fechaCompletada: String?
)