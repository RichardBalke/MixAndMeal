package api.utils

import api.models.User
import api.models.UserResponse

fun User.toResponse() = UserResponse(id, email, role)