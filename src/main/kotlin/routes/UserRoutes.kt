package api.routes

import api.models.Role
import api.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import service.AuthorizationService


fun Route.userRoutes(userRepository: UserRepository) {
    val authService = AuthorizationService()
    route("/users") {
        authenticate {
            get {

                val users = userRepository.findAll()
                call.respond(users)
            }

            get("/{id}") {
                // controleert of de rol van de gebruiker ADMIN is
                if (!authService.hasRole(call, Role.ADMIN)) {
                    call.respond(HttpStatusCode.Forbidden, "Geen toegang")
                    return@get
                }
                // controleert of de parameter {id} in de url naar een Long type geconvert kan worden.
                val id: Long = call.parameters["id"]?.toLongOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                // controleert of de user met 'id' bestaat
                val user = userRepository.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(HttpStatusCode.OK, user)


            }
        }
    }
}