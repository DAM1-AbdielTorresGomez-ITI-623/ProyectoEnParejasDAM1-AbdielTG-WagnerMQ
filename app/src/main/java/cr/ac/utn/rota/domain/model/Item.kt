package cr.ac.utn.rota.domain.model

data class Item(
    val id: String,
    val donorId: String,
    val title: String,
    val description: String,
    val category: ItemCategory,
    val status: ItemStatus,
    val condition: String,
    val generalLocation: String,
    val imageUris: List<String> = emptyList()
)
