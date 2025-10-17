package api.models

sealed class AuthResult {
    data class Success(val token: String, val user: User) : AuthResult()
    data class Failure(val reason: AuthError) : AuthResult()
}

