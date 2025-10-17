package api.models

data class SignUpRequest(val email: String, val password: String)
data class SignInRequest(val email: String, val password: String)
data class TokenResponse(val accessToken: String, val tokenType: String = "Bearer", val expiresInSeconds: Long)
data class UserResponse(val id: Long, val email: String, val roles: Role)
data class ErrorResponse(val code: String, val message: String)
