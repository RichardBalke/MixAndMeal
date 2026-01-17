package routes

import api.requests.RecipeIDRequest
import api.responses.RecipeCardResponse
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import service.UserDietsService
import models.dto.UserDietEntry
import io.ktor.server.routing.Route
import io.ktor.http.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import models.dto.DietEntry
import org.koin.ktor.ext.inject
import service.DietsService
import service.RecipeDietsService

fun Route.userDietsRoutes() {
    val userDietsService by inject<UserDietsService>()
    val dietsService by inject<DietsService>()
    val recipeDietsService by inject<RecipeDietsService>()

    route("delete-diet"){
        post{
            val id = call.receive<RecipeIDRequest>().recipeId
            val check = recipeDietsService.deleteAllRecipeDiets(id)
            call.respond(HttpStatusCode.OK, check)
        }
    }
    authenticate {
        route("/user-diets") {
            get() {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!
                val userDiets: List<UserDietEntry> = userDietsService.getUserDietEntries(userId)
                val diets = mutableListOf<DietEntry?>()

                if (userDiets.isNotEmpty()) {
                    diets.addAll(userDietsService.getDietsFromEntries(userDiets, dietsService))
                }

                call.respond(HttpStatusCode.OK, diets)
            }

            post("/add-diet") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val dietId = call.receive<DietEntry>()

                try {
                    val entry = userDietsService.addUserDietEntry(
                        UserDietEntry(userId = userId, dietId = dietId.id)
                    )
                    val userDiets: List<UserDietEntry> = userDietsService.getUserDietEntries(userId)
                    val diets = mutableListOf<DietEntry?>()

                    if (userDiets.isNotEmpty()) {
                        diets.addAll(userDietsService.getDietsFromEntries(userDiets, dietsService))
                    }
                    call.respond(HttpStatusCode.Created, diets)

                } catch (e: Exception) {
                    val userDiets: List<UserDietEntry> = userDietsService.getUserDietEntries(userId)
                    val diets = mutableListOf<DietEntry?>()

                    if (userDiets.isNotEmpty()) {
                        diets.addAll(userDietsService.getDietsFromEntries(userDiets, dietsService))
                    }
                    call.respond(HttpStatusCode.Conflict, diets)
                }
            }

            delete("/remove-diet") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.getClaim("userId", String::class)!!

                val dietId = call.receive<DietEntry>()

                userDietsService.removeUserDietEntry(userId, dietId.id)
                val userDiets: List<UserDietEntry> = userDietsService.getUserDietEntries(userId)
                val diets = mutableListOf<DietEntry?>()

                if (userDiets.isNotEmpty()) {
                    diets.addAll(userDietsService.getDietsFromEntries(userDiets, dietsService))
                }
                call.respond(HttpStatusCode.OK, diets)
            }
        }
    }
}