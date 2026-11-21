package cr.ac.utn.rota.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.rota.domain.model.Item
import cr.ac.utn.rota.domain.model.ItemStatus
import cr.ac.utn.rota.domain.repository.IItemRepository

enum class ItemError {
    DUPLICATE, NOT_FOUND, NOT_OWNER, INVALID_DATA, INVALID_STATUS
}

class ItemValidationException(val error: ItemError): Exception()

class ItemViewModel(private val repository: IItemRepository): ViewModel() {
    fun getItems(): List<Item> {
        return repository.getItems()
    }

    fun getItemById(id: String): Item? {
        return repository.getItemById(id)
    }

    fun getMyItems(userId: String): List<Item> {
        return repository.getItems().filter { it.donorId == userId }
    }

    fun getMyItemById(id: String, userId: String): Item {
        val item = repository.getItemById(id)
            ?: throw ItemValidationException(ItemError.NOT_FOUND)
        if (item.donorId != userId)
            throw ItemValidationException(ItemError.NOT_OWNER)
        return item
    }

    fun createItem(item: Item, userId: String) {
        validateItem(item)
        if (item.donorId != userId)
            throw ItemValidationException(ItemError.NOT_OWNER)
        if (item.status != ItemStatus.AVAILABLE)
            throw ItemValidationException(ItemError.INVALID_STATUS)
        if (repository.getItemById(item.id) != null)
            throw ItemValidationException(ItemError.DUPLICATE)
        repository.addItem(item)
    }

    fun updateItem(item: Item, userId: String) {
        val saved = getMyItemById(item.id, userId)
        if (item.donorId != saved.donorId)
            throw ItemValidationException(ItemError.NOT_OWNER)
        if (item.status != saved.status)
            throw ItemValidationException(ItemError.INVALID_STATUS)
        if (item.imageUris != saved.imageUris)
            throw ItemValidationException(ItemError.INVALID_DATA)
        validateItem(item)
        repository.updateItem(item)
    }

    fun removeItem(id: String, userId: String) {
        getMyItemById(id, userId)
        repository.deleteItem(id)
    }

    fun restoreItem(item: Item, userId: String) {
        if (item.donorId != userId)
            throw ItemValidationException(ItemError.NOT_OWNER)
        if (repository.getItemById(item.id) != null)
            throw ItemValidationException(ItemError.DUPLICATE)
        validateItem(item)
        repository.addItem(item)
    }

    private fun validateItem(item: Item) {
        if (item.id.isBlank() || item.donorId.isBlank() || item.title.isBlank() ||
            item.description.isBlank() || item.condition.isBlank() ||
            item.description.length > 500)
            throw ItemValidationException(ItemError.INVALID_DATA)
    }
}
