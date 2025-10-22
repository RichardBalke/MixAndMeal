package api.routes

import api.models.Allergens
import api.models.Ingredients
import api.repository.IngredientsRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.ingredientsRoutes(repository: IngredientsRepository) {

    route("/ingredients") {

        // GET all ingredients
        get {
            call.respond(repository.findAll())
        }


        // GET by ID
        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                return@get
            }
            val ingredient = repository.findById(id)
            if (ingredient == null) {
                call.respond(HttpStatusCode.NotFound, "Ingredient not found")
            } else {
                call.respond(ingredient)
            }
        }

        // GET by name
        get("/name/{name}") {
            val name = call.parameters["name"]
            val ingredient = name?.let { repository.findByName(it) }
            if (ingredient == null) {
                call.respond(HttpStatusCode.NotFound, "Ingredient not found")
            } else {
                call.respond(ingredient)
            }
        }

        authenticate {
            // POST create
            post {
                val request = call.receive<Ingredients>()
                val created = repository.create(request)
                call.respond(HttpStatusCode.Created, created)
            }

            // PUT update full ingredient
            put {
                val request = call.receive<Ingredients>()
                repository.update(request)
                call.respond(HttpStatusCode.OK, "Ingredient updated")
            }

            // PATCH update allergens
            patch("/{id}/allergens") {
                val id = call.parameters["id"]?.toLongOrNull()
                val newAllergens = call.receive<List<Allergens>>()
                val ingredient = id?.let { repository.findById(it) }

                if (ingredient == null) {
                    call.respond(HttpStatusCode.NotFound, "Ingredient not found")
                    return@patch
                }

                val updated = repository.updateAllergens(ingredient, newAllergens)
                call.respond(updated)
            }

            // DELETE ingredient
            delete("/{id}") {
                val id = call.parameters["id"]?.toLongOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@delete
                }
                val success = repository.delete(id)
                if (success) {
                    call.respond(HttpStatusCode.OK, "Ingredient deleted")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Ingredient not found")
                }
            }
        }
    }
}