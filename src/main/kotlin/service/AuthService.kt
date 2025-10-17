package api.service

import api.models.User
import api.models.AuthResult
import api.models.TokenResponse

interface TokenProvider {
    fun generateToken(user: User): String
    fun validateToken(token: String): Long? // returns userId or null
}

interface AuthService {
    suspend fun signUp(email: String, plainPassword: String): AuthResult
    suspend fun signIn(email: String, plainPassword: String): AuthResult
    suspend fun me(userId: Long): User?
}
