package routes

import api.requests.DietsIDRequest
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

                val dietId = call.receive<DietsIDRequest>()

                try {
                    val diet = dietsService.getDietsByDisplayName(dietId.displayName)
                    if (diet != null) {
                        val entry = userDietsService.addUserDietEntry(
                            UserDietEntry(userId = userId, dietId = diet.id)
                        )
                        val userDiets: List<UserDietEntry> = userDietsService.getUserDietEntries(userId)
                        val diets = mutableListOf<DietEntry?>()

                        if (userDiets.isNotEmpty()) {
                            diets.addAll(userDietsService.getDietsFromEntries(userDiets, dietsService))
                        }
                        call.respond(HttpStatusCode.Created, diets)
                    }else{
                        call.respond(HttpStatusCode.NotFound)
                    }

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

                val dietId = call.receive<DietsIDRequest>()

                val diet = dietsService.getDietsByDisplayName(dietId.displayName)
                if (diet != null) {
                    userDietsService.removeUserDietEntry(userId, diet.id)
                    val userDiets: List<UserDietEntry> = userDietsService.getUserDietEntries(userId)
                    val diets = mutableListOf<DietEntry?>()

                    if (userDiets.isNotEmpty()) {
                        diets.addAll(userDietsService.getDietsFromEntries(userDiets, dietsService))
                    }
                    call.respond(HttpStatusCode.OK, diets)
                }
                else{
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}