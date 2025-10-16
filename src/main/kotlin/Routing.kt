package api

import api.repository.FakeIngredientsRepository
import api.repository.FakeUserRepository
import api.routes.ingredientsRoutes
import api.routes.userRoutes
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import routes.authRoutes

fun Application.configureRouting() {
    routing {
        userRoutes(FakeUserRepository)
        ingredientsRoutes(FakeIngredientsRepository)
        authRoutes("secret_key", "http://localhost:8080/","jwt-audience")
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
