package routes

import api.repository.FakeUserRepository
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import models.Login

fun Route.authRoutes(jwtSecret: String, jwtIssuer: String, jwtAudience: String) {
    post("/login"){
        val req = call.receive<Login>()
        val user = FakeUserRepository.users.find {
            it.email == req.email && it.password == req.password
        }
        if (user != null) {
            val token = JWT.create()
                .withAudience(jwtAudience)
                .withIssuer(jwtIssuer)
                .withClaim("username", user.email)
                .withClaim("role", user.role.name)
                .sign(Algorithm.HMAC256(jwtSecret))
            call.respond(mapOf("token" to token))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Ongeldige gegevens")
        }
    }
}