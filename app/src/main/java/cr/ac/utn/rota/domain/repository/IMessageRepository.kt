package cr.ac.utn.rota.domain.repository

import cr.ac.utn.rota.domain.model.ChatMessage

interface IMessageRepository {
    fun getMessages(requestId: String): List<ChatMessage>
    fun addMessage(message: ChatMessage)
}
