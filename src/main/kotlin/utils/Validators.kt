package api.utils

import api.models.SignInRequest
import api.models.SignUpRequest

object Validators {
    fun validateSignUp(req: SignUpRequest): String? {
        if (!req.email.contains('@')) return "Invalid email"
        if (req.password.length < 8) return "Password must be at least 8 characters"
        return null
    }
    fun validateSignIn(req: SignInRequest): String? {
        if (req.email.isBlank()) return "Email required"
        if (req.password.isBlank()) return "Password required"
        return null
    }
}
