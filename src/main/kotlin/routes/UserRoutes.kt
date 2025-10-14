package api.routes

import api.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.userRoutes(userRepository: UserRepository) {
    route("/users") {
        get {
            val users = userRepository.findAll()
            call.respond(users)
        }

        get("/{id}") {
            val id: Long = call.parameters["id"]?.toLongOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest)

            val user = userRepository.findById(id)
                ?: return@get call.respond(HttpStatusCode.NotFound)

            call.respond(HttpStatusCode.OK, user)


        }
    }
}