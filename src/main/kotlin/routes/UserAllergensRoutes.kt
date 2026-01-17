package routes

import api.requests.AllergenIDRequest
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserAllergensService
import models.dto.UserAllergenEntry
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import org.koin.ktor.ext.inject
import service.AllergenService

fun Route.userAllergensRoutes() {
    val userAllergensService by inject<UserAllergensService>()
    val allergensService by inject<AllergenService>()

    authenticate {
        route("/allergens") {

            // GET /allergens
            get {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val allergens = userAllergensService.getUserAllergenEntries(userId)
                call.respond(HttpStatusCode.OK, allergens)
            }

            // POST /allergens/add-allergen
            post("/add-allergen") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val request = call.receive<AllergenIDRequest>()

                try {
                    val allergen = allergensService.getAllergenByDisplayName(request.displayName)
                    if (allergen == null) {
                        call.respond(HttpStatusCode.NotFound)
                    }else {

                        userAllergensService.addUserAllergenEntry(userId, allergen.id)
                        val allergens = userAllergensService.getUserAllergenEntries(userId)
                        call.respond(HttpStatusCode.Created, allergens)
                    }
                } catch (e: Exception) {
                    val allergens = userAllergensService.getUserAllergenEntries(userId)
                    call.respond(HttpStatusCode.Conflict, allergens)
                }
            }

            // DELETE /allergens/remove-allergen
            delete("/remove-allergen") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val request = call.receive<AllergenIDRequest>()
                val allergen = allergensService.getAllergenByDisplayName(request.displayName)
                if (allergen == null) {
                    call.respond(HttpStatusCode.NotFound)
                }else {
                    userAllergensService.removeUserAllergenEntry(userId, allergen.id)

                    val allergens = userAllergensService.getUserAllergenEntries(userId)
                    call.respond(HttpStatusCode.OK, allergens)
                }
            }
        }
    }
}
