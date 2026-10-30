package cr.ac.utn.rota.domain.repository

import cr.ac.utn.rota.domain.model.User

interface IUserRepository {
    fun getUsers(): List<User>
    fun getUserById(id: String): User?
    fun addUser(user: User)
}
