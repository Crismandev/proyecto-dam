package pe.edu.cibertec.tech_mentor.domain.model

data class Mission(
    val idMision: Long,
    val nombre: String,
    val descripcion: String,
    val tipo: String,
    val meta: Int,
    val xpRecompensa: Int,
    val fechaInicio: String?,
    val fechaFin: String?,
    val activa: Boolean
)