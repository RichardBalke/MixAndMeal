package api.routes

import api.models.Diets
import api.models.Difficulty
import api.repository.RecipesRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.recipesRoutes(repository: RecipesRepository) {

    route("/recipes") {

        // Get all recipes
        get {
            call.respond(repository.findAll())
        }

        //Get recipes by title
        get("/title/{title}") {
            val name = call.parameters["title"].toString()
            val title = repository.findByTitle(name)
            if (title == null) {
                call.respond(HttpStatusCode.NotFound, "Recipe not found.")
            } else {
                call.respond(title)
            }
        }

        //Get by difficulty
        get("/difficulty/{difficulty}") {
            val diff = call.parameters["difficulty"]

            val difficulty = try {
                diff?.let { Difficulty.valueOf(it.uppercase()) }
            } catch (e: IllegalArgumentException) {
                null
            }

            if (difficulty == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid difficulty level.")
                return@get
            }

            val diffRecipes = repository.findByDifficulty(difficulty)
            call.respond(diffRecipes)
        }

        //Get by mealtype
        get("/mealtype/{mealtype}") {
            val type = call.parameters["mealtype"]?.lowercase()

            if (type == null) {
                call.respond(HttpStatusCode.BadRequest)
            }

            val recipes = repository.findByMealType(type)

            if (recipes.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "No recipes found for meal type $type.")
            } else {
                call.respond(recipes)
            }
        }

        //Get by diets
        get("/diet/{diet}") {
            fun String?.toDietOrNull(): Diets? {
                if (this == null) {
                    return null
                }

                val readableDietText = this.uppercase().replace(" ", "_")
                return Diets.entries.firstOrNull { it.name == readableDietText }
            }

            val getDietType = call.parameters["mealtype"]
            val diet = getDietType.toDietOrNull()
            if (diet == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid diet.")
                return@get
            }
            val recipesByDiet = repository.findByDiets(diet)
            call.respond(recipesByDiet)
        }

        //Get by kitchenstyle
//        get("/{kitchen}") {
//
//        }


    }

}
