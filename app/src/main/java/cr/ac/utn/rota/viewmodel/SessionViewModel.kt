package cr.ac.utn.rota.viewmodel

import androidx.lifecycle.ViewModel
import cr.ac.utn.rota.domain.model.User
import cr.ac.utn.rota.domain.repository.IUserRepository

class SessionViewModel(private val repository: IUserRepository): ViewModel() {
    var activeUser: User? = null

    fun getUsers(): List<User> {
        return repository.getUsers()
    }

    fun addUser(user: User) {
        repository.addUser(user)
    }

    fun selectUser(id: String) {
        activeUser = repository.getUserById(id)
    }
}
