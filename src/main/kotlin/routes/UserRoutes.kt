package api.routes

import api.models.Role
import api.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import routes.requireAdmin
import service.UserService


fun Route.userRoutes(userService: UserService) {
    // authenticate zorgt ervoor dat alleen ingelogde users de routes kunnen gebruiken.
    authenticate{
        route("/users") {

            get {
                // Deze if else statement check of de ingelogde user een admin rol heeft
                if(call.requireAdmin()){
                    val users = userService.findAll()
                    call.respond(users)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }

            }

            get("/{id}") {
                // controleert of de parameter {id} in de url naar een Long type geconvert kan worden.
                val id: Long = call.parameters["id"]?.toLongOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest)

                // controleert of de user met 'id' bestaat
                val user = userService.findById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound)

                call.respond(HttpStatusCode.OK, user)


            }

        }
    }
}