package api.routes

import api.requests.IngredientIDRequest
import api.service.IngredientService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserFridgeService
import models.dto.UserFridgeEntry
import io.ktor.server.routing.Route
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import org.koin.ktor.ext.inject

fun Route.userFridgeRoutes() {
    val userFridgeService by inject<UserFridgeService>()
    val ingredientService by inject<IngredientService>()
    authenticate {
        route("/fridge") {
            get() {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val fridge: List<UserFridgeEntry> = userFridgeService.getUserFridgeEntries(userId)

                call.respond(HttpStatusCode.OK ,fridge)
            }

            post("/add-ingredient-to-fridge") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val ingredient = call.receive<IngredientIDRequest>()
                val ingredientExists = ingredientService.getIngredientsByName(ingredient.ingredientName)
                if(ingredientExists == null){
                    call.respond(HttpStatusCode.NotFound)
                }
                try {
                    userFridgeService.addUserFridgeEntry(
                        UserFridgeEntry(userId = userId, ingredientName = ingredient.ingredientName)
                    )
                    val fridge: List<UserFridgeEntry> = userFridgeService.getUserFridgeEntries(userId)
                    call.respond(HttpStatusCode.Created, fridge)
                } catch (e: Exception) {
                    val fridge: List<UserFridgeEntry> = userFridgeService.getUserFridgeEntries(userId)
                    call.respond(HttpStatusCode.Conflict, fridge)
                }
            }

            delete("/remove-ingredient-from-fridge") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val ingredient = call.receive<IngredientIDRequest>()

                userFridgeService.removeUserFridgeEntry(userId, ingredient.ingredientName)
                val fridge: List<UserFridgeEntry> = userFridgeService.getUserFridgeEntries(userId)

                call.respond(HttpStatusCode.OK, fridge)
            }
        }
    }

}