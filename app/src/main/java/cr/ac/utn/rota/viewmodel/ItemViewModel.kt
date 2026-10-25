package cr.ac.utn.rota.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.rota.domain.model.Item
import cr.ac.utn.rota.domain.repository.IItemRepository

class ItemViewModel(private val repository: IItemRepository): ViewModel() {

    fun getItems(): List<Item> {
        return repository.getItems()
    }

    fun getItemById(id: String): Item? {
        return repository.getItemById(id)
    }

    fun createItem(item: Item) {
        if (repository.getItemById(item.id) != null)
            throw Exception("The item already exists.")
        repository.addItem(item)
    }

    fun updateItem(item: Item) {
        if (repository.getItemById(item.id) == null)
            throw Exception("The item does not exist.")
        repository.updateItem(item)
    }

    fun removeItem(id: String) {
        repository.deleteItem(id)
    }
}
