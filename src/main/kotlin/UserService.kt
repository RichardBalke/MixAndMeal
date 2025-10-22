package service

import api.models.User

interface UserService {
    suspend fun create(user: User): User
    suspend fun findById(id: Long): User?
    suspend fun findByUsername(username: String): User?
    suspend fun findAll(): List<User>
    suspend fun delete(id: Long): Boolean
    suspend fun update(user: User): Boolean
}
