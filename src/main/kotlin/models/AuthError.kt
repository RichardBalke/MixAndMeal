package api.models

enum class AuthError {
    INVALID_CREDENTIALS,
    USER_NOT_FOUND,
    ALREADY_EXISTS,
    DOMAIN_ERROR
}