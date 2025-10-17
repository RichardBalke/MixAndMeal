package api.repository

import api.models.User

interface AuthRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: Long): User?
    suspend fun create(user: User): User
    suspend fun update(user: User): User
    suspend fun delete(id: Long): Boolean
}
