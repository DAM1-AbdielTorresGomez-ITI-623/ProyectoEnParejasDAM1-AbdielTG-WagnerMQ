package cr.ac.utn.rota.domain.model

data class User(
    val id: String,
    val name: String,
    val role: UserRole,
    val generalLocation: String,
    val username: String? = null
)
