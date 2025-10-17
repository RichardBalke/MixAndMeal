package api.utils

import api.models.AuthError
import api.models.ErrorResponse

fun mapAuthError(err: AuthError): ErrorResponse = when (err) {
    AuthError.INVALID_CREDENTIALS -> ErrorResponse("invalid_credentials", "Invalid email or password")
    AuthError.USER_NOT_FOUND -> ErrorResponse("user_not_found", "User not found")
    AuthError.ALREADY_EXISTS -> ErrorResponse("already_exists", "User already exists")
    else -> ErrorResponse("server_error", "Unknown server error")
}
