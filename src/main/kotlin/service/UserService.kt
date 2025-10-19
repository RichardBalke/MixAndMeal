package service

import api.models.Role
import api.models.User
import api.repository.FakeUserRepository.users
import api.repository.FakeUserRepository.currentID
import api.repository.UserRepository


class UserService : UserRepository {

    // Creëert een user, voegt daar een ID aan toe en returned deze.
    override suspend fun create(entity: User): User {
        currentID++
        val newUser = entity.copy(id = currentID)
        users.add(newUser)
        return newUser
    }


    //Get functies
    override suspend fun findById(id: Long): User? {
        return users.find { it.id == id }
    }

    override suspend fun findByUsername(username: String): User? {
        return users.find { it.name == username }
    }

    override suspend fun findAll(): List<User> {
        return users.toList()
    }

    override suspend fun delete(id: Long): Boolean {
        return users.removeIf { id == it.id}
    }

    override suspend fun update(entity: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getRoleById(id: Long): Role {
        return findById(id)?.role ?: Role.USER
    }
}