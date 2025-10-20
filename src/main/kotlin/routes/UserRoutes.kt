 package api.routes

import api.models.Role
import api.models.User
import api.repository.FakeRecipeRepository.recipeService
import api.repository.UserRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.delete
import routes.requireAdmin
import service.UserService
import io.ktor.server.application.*


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

            get("/favourites/{id}") {}

            post("/favourites/{id}/{recipeId}") {
                val id = call.parameters["id"]?.toLongOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest)
                val recipeId = call.parameters["recipeId"]?.toLongOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest)

                val recipe = recipeService.findById(id)

                if (recipe != null) {
                    val updatedUser = userService.addFavourite(id, recipe)
                    if (updatedUser != null) {
                        call.respond(HttpStatusCode.OK, updatedUser.favourites)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }

//            delete("/{id}/favourites") {
//                val id = call.parameters["id"]?.toLongOrNull()
//                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
//
//                val recipeId = call.receive<Long>()
//
//                val updatedUser = userService.removeFavourite(id, recipeId)
//                    ?: return@delete call.respond(HttpStatusCode.NotFound, "User not found")
//
//                call.respond(HttpStatusCode.OK, updatedUser.favourites)
//            }

            post {
                val newUser = call.receive<User>()
                val created = userService.create(newUser)
                call.respond(HttpStatusCode.Created, created)
            }

        }
    }
}