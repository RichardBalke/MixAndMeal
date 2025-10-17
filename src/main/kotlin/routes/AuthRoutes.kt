package api.routes

import api.models.ErrorResponse
import api.models.SignInRequest
import api.models.SignUpRequest
import api.models.TokenResponse
import api.models.UserResponse
import api.service.AuthService
import api.utils.Validators
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes(authService: AuthService) {
    route("/auth") {
        post("/signup") {
            val req = call.receive<SignUpRequest>()
            val validation = Validators.validateSignUp(req)
            if (validation != null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("validation_error", validation))
                return@post
            }
            when (val result = authService.signUp(req.email, req.password)) {
                is api.models.AuthResult.Success -> {
                    val tokenResp = TokenResponse(result.token, "Bearer", expiresInSeconds = 3600)
                    val userResp = UserResponse(result.user.id, result.user.email, result.user.role)
                    call.respond(HttpStatusCode.Created, mapOf("token" to tokenResp, "user" to userResp))
                }
                is api.models.AuthResult.Failure -> {
                    val err = when (result.reason) {
                        api.models.AuthError.ALREADY_EXISTS ->
                            ErrorResponse("already_exists", "User with that email already exists")
                        else -> ErrorResponse("auth_error", "Authentication failed")
                    }
                    call.respond(HttpStatusCode.Conflict, err)
                }
            }
        }

        post("/signin") {
            val req = call.receive<SignInRequest>()
            val validation = Validators.validateSignIn(req)
            if (validation != null) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse("validation_error", validation))
                return@post
            }
            when (val result = authService.signIn(req.email, req.password)) {
                is api.models.AuthResult.Success -> {
                    val tokenResp = TokenResponse(result.token, "Bearer", expiresInSeconds = 3600)
                    val userResp = UserResponse(result.user.id, result.user.email, result.user.role)
                    call.respond(HttpStatusCode.OK, mapOf("token" to tokenResp, "user" to userResp))
                }
                is api.models.AuthResult.Failure -> {
                    val err = when (result.reason) {
                        api.models.AuthError.USER_NOT_FOUND ->
                            ErrorResponse("not_found", "User not found")
                        api.models.AuthError.INVALID_CREDENTIALS ->
                            ErrorResponse("invalid_credentials", "Invalid email or password")
                        else -> ErrorResponse("auth_error", "Authentication failed")
                    }
                    call.respond(HttpStatusCode.Unauthorized, err)
                }
            }
        }

        authenticate("jwt") {
            get("/me") {
                val principal = call.principal<UserIdPrincipal>() ?: run {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }
                val userId = (principal.name)
                val user = authService.me(userId)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("not_found", "User not found"))
                    return@get
                }
                call.respond(HttpStatusCode.OK, UserResponse(user.id, user.email, user.role))
            }
        }
    }
}
