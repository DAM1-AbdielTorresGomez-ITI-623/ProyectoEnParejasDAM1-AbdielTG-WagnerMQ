package cr.ac.utn.rota.domain.repository

import cr.ac.utn.rota.domain.model.Item

interface IItemRepository {
    fun getItems(): List<Item>
    fun getItemById(id: String): Item?
    fun addItem(item: Item)
    fun updateItem(item: Item)
    fun deleteItem(id: String)
}
