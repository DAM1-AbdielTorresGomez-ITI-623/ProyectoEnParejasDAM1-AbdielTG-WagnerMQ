package cr.ac.utn.rota.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.rota.domain.model.ChatMessage
import cr.ac.utn.rota.domain.repository.IMessageRepository

class ChatViewModel(private val repository: IMessageRepository): ViewModel() {

    fun getMessages(requestId: String): List<ChatMessage> {
        return repository.getMessages(requestId)
    }

    fun sendMessage(message: ChatMessage) {
        repository.addMessage(message)
    }
}
