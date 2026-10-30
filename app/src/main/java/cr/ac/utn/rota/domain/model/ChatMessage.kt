package cr.ac.utn.rota.domain.model

import java.time.LocalDateTime

data class ChatMessage(
    val id: String,
    val requestId: String,
    val senderId: String,
    val text: String,
    val sentAt: LocalDateTime
)
