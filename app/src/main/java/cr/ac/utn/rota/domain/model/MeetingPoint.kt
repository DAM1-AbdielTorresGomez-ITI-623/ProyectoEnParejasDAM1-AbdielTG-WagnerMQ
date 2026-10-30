package cr.ac.utn.rota.domain.model

data class MeetingPoint(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val description: String? = null
)
