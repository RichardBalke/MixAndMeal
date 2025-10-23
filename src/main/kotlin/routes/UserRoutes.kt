package api.routes

import api.models.Recipes
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
import service.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receiveNullable
import service.authenticatedUserId
import service.requireAdmin


fun Route.userRoutes(userService: UserService) {
    // authenticate zorgt ervoor dat alleen ingelogde users de routes kunnen gebruiken.
    authenticate {
        route("/users") {

            get {
                // Deze if else statement check of de ingelogde user een admin rol heeft
                if (call.requireAdmin()) {
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

                if (id == call.authenticatedUserId()) {
                    val user = userService.findById(id)
                        ?: return@get call.respond(HttpStatusCode.NotFound)

                    call.respond(HttpStatusCode.OK, user)
                } else if (call.requireAdmin()) {
                    val user = userService.findById(id)
                        ?: return@get call.respond(HttpStatusCode.NotFound)

                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }

            post("/favo"){
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)?.toLongOrNull()
                    ?: return@post call.respond(HttpStatusCode.BadRequest, "User is not in token")

                val user = userService.findById(userId)
                    ?: return@post call.respond(HttpStatusCode.NotFound, "user not found")

                val request = call.receiveNullable<Recipes>() ?: kotlin.run{
                    call.respond(HttpStatusCode.BadRequest)
                    return@post
                }

                if(user.favourites.any{it.id == request.id }) {
                    user.favourites.remove(request)
                    call.respond(HttpStatusCode.Accepted, user.favourites)
                }
                else{
                    user.favourites.add(request)
                    call.respond(HttpStatusCode.Accepted, user.favourites)
                }

            }

            //
//            get("/favourites/{id}") {
//                val principal = call.principal<JWTPrincipal>()
//                val userId = principal?.getClaim("userId", String::class)?.toLong()
//
//                val recipeId = call.parameters["id"]?.toLongOrNull()
//                    ?: return@get call.respond(HttpStatusCode.BadRequest)
//
//                val recipe = recipeService.findById(recipeId)
//                if (recipe == null) {
//                    return@get call.respond(HttpStatusCode.NotFound, "Recipe not found.")
//                }
//
//                if (userId != null) {
//                    val updatedUser = userService.addFavourite(userId, recipe)
//
//                    if (updatedUser != null) {
//                        call.respond(HttpStatusCode.OK, updatedUser.favourites)
//                    }
//                } else {
//                    call.respond(HttpStatusCode.NotFound, "User not found.")
//                }
//            }

//            post("/favourites/{recipeId}") {
//                val principal = call.principal<JWTPrincipal>()
//                val userId = principal?.getClaim("userId", String::class)?.toLong()
//
//                val recipeId = call.parameters["recipeId"]?.toLongOrNull()
//                    ?: return@post call.respond(HttpStatusCode.BadRequest)
//
//                if (recipeId != null) {
//                    val recipe = recipeService.findById(recipeId)
//
//                    if (recipe != null && userId != null) {
//                        val updatedUser = userService.addFavourite(userId, recipe)
//                        if (updatedUser != null) {
//                            call.respond(HttpStatusCode.OK, updatedUser.favourites)
//                        } else {
//                            call.respond(HttpStatusCode.Forbidden)
//                        }
//                    }
//                } else {
//                    call.respond(HttpStatusCode.NotFound, "recipe id: $recipeId")
//                }
//
//            }

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