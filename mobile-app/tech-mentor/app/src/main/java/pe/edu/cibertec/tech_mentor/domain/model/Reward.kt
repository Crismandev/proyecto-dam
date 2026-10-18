package pe.edu.cibertec.tech_mentor.domain.model

data class Reward(
    val idRecompensa: Long,
    val nombre: String,
    val descripcion: String,
    val tipo: String,
    val cantidad: Int,
    val costoMonedas: Int,
    val diaSecuenciaRequerido: Int?,
    val activa: Boolean
)