package pe.edu.cibertec.tech_mentor.domain.model

data class UserMission(
    val idMisionUsuario: Long,
    val idUsuario: Long,
    val idMision: Long,
    val nombreMision: String,
    val meta: Int,
    val progresoActual: Int,
    val completada: Boolean,
    val fechaAsignacion: String?,
    val fechaCompletada: String?
)