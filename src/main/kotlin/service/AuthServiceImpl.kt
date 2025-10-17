package api.service

import api.models.AuthError
import api.models.AuthResult
import api.models.Role
import api.models.User
import api.repository.AuthRepository
import org.gradle.internal.impldep.com.jcraft.jsch.jbcrypt.BCrypt


class AuthServiceImpl(
    private val repo: AuthRepository,
    private val tokenProvider: TokenProvider,
) : AuthService {

    override suspend fun signUp(email: String, plainPassword: String): AuthResult {
        val normalized = email.trim().lowercase()
        if (repo.findByEmail(normalized) != null) return AuthResult.Failure(AuthError.ALREADY_EXISTS)
        val hashed = hashPassword(plainPassword)
        val user = User("Bart", hashed, "test1@test.nl", Role.ADMIN)

        val created = repo.create(user)
        val token = tokenProvider.generateToken(created)
        return AuthResult.Success(token, created)
    }

    override suspend fun signIn(email: String, plainPassword: String): AuthResult {
        val normalized = email.trim().lowercase()
        val user = repo.findByEmail(normalized) ?: return AuthResult.Failure(AuthError.USER_NOT_FOUND)
        if (!verifyPassword(plainPassword, user.password)) return AuthResult.Failure(AuthError.INVALID_CREDENTIALS)
        val token = tokenProvider.generateToken(user)
        return AuthResult.Success(token, user)
    }

    override suspend fun me(userId: Long): User? = repo.findById(userId)

    private fun hashPassword(pw: String) = BCrypt.hashpw(pw, BCrypt.gensalt(12))
    private fun verifyPassword(plain: String, hashed: String) = BCrypt.checkpw(plain, hashed)
}