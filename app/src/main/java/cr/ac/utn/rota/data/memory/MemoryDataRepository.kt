package cr.ac.utn.rota.data.memory

import cr.ac.utn.rota.domain.model.*
import cr.ac.utn.rota.domain.repository.IRotaRepository

class MemoryDataRepository: IRotaRepository {
    private val users = mutableListOf<User>()
    private val items = mutableListOf<Item>()
    private val requests = mutableListOf<DonationRequest>()
    private val messages = mutableListOf<ChatMessage>()

    override fun getUsers() = users

    override fun getUserById(id: String) = users.find { it.id == id }

    override fun addUser(user: User) {
        users.add(user)
    }

    override fun getItems() = items

    override fun getItemById(id: String) = items.find { it.id == id }

    override fun addItem(item: Item) {
        items.add(item)
    }

    override fun updateItem(item: Item) {
        deleteItem(item.id)
        addItem(item)
    }

    override fun deleteItem(id: String) {
        items.removeIf { it.id == id }
    }

    override fun getRequests() = requests

    override fun getRequestById(id: String) = requests.find { it.id == id }

    override fun addRequest(request: DonationRequest) {
        requests.add(request)
    }

    override fun updateRequest(request: DonationRequest) {
        requests.removeIf { it.id == request.id }
        addRequest(request)
    }

    override fun getMessages(requestId: String) = messages.filter { it.requestId == requestId }

    override fun addMessage(message: ChatMessage) {
        messages.add(message)
    }
}
