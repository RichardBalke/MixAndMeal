package api.routes

import api.repository.IngredientUnitRepositoryImpl
import api.repository.RecipesRepositoryImpl
import api.requests.RecipeIDRequest
import api.requests.RecipeUploadRequest
import api.responses.RecipeCardResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import models.dto.IngredientEntry
import models.dto.RecipeEntry
import org.koin.ktor.ext.inject
import repository.AllergensRepository
import api.responses.RecipeResponse
import api.service.ByteArraySourceFile
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.request.receiveMultipart
import kotlinx.serialization.json.Json
import net.schmizz.sshj.SSHClient
import repository.DietsRepository
import requests.RecipeSearchRequest
import responses.FullRecipeScreenResponse
import service.AllergenService
import service.DietsService
import service.IngredientUnitService
import service.RecipeAllergenService
import service.RecipeDietsService
import service.RecipeImagesService
import service.RecipeService
import service.requireAdmin
import java.io.ByteArrayInputStream

fun Route.fullRecipe() {
    route("/fullrecipe") {
        val recipeService by inject<RecipeService>()
        val recipeImagesService by inject<RecipeImagesService>()
        val recipeDietService by inject<RecipeDietsService>()
        val dietsService by inject<DietsService>()
        val recipeAllergenService by inject<RecipeAllergenService>()
        val allergenService by inject<AllergenService>()
        val ingredientUnitService by inject<IngredientUnitService>()
        get("/{recipeId}"){
            val id = call.parameters["recipeId"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)

            val recipe = recipeService.getRecipe(id) ?: return@get call.respond(HttpStatusCode.NotFound, "Recipe not found")
            val recipeImages = recipeImagesService.getImagesForRecipe(id) ?: return@get call.respond(HttpStatusCode.NotFound, "Image not found")
            val diets = recipeDietService.getDietsbyRecipeId(id, dietsService)
            val allergens = recipeAllergenService.getAllergensByRecipeId(id, allergenService)
            val ingredients = ingredientUnitService.getIngredientsByRecipeId(id)

            val fullRecipe = FullRecipeScreenResponse(
                recipe.id?.toInt() ?: 0,
                recipe.title,
                recipe.description,
                recipe.instructions,
                recipe.prepTime,
                recipe.cookingTime,
                recipe.difficulty,
                recipeImages,
                recipe.mealType,
                recipe.kitchenStyle,
                diets,
                allergens,
                ingredients
            )
            call.respond(HttpStatusCode.OK, fullRecipe)
        }
    }
}

fun Route.featuredRecipeDetails(){
    route("/recipes/featured"){
        get("/{recipeId}") {
            val id = call.parameters["recipeId"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val recipeService by inject<RecipeService>()
            val recipeImagesService by inject<RecipeImagesService>()
            val recipe = recipeService.getRecipe(id) ?: return@get call.respond(HttpStatusCode.NotFound, "Recipe not found")
            val recipeImages = recipeImagesService.getImagesForRecipe(id).toMutableList()
            val response : RecipeCardResponse = RecipeCardResponse(
                recipe.id?.toInt() ?: 0,
                recipe.title,
                recipe.description,
                recipe.cookingTime,
                recipeImages
            )
            call.respond(HttpStatusCode.OK, response)
        }
    }
}

fun Route.popularRecipes(){
    val recipeService by inject<RecipeService>()
    val recipeImagesService by inject<RecipeImagesService>()
    route("/popular-recipes"){
        get("/{limit}"){
            val limit = call.parameters["limit"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val response = recipeService.findPopularRecipes(limit, recipeImagesService)
            if(response.isEmpty()){
                call.respond(HttpStatusCode.NoContent, "Recipes not found")
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}

fun Route.recipeCardsByDifficulty(){
    val recipeService by inject<RecipeService>()
    val recipeImagesService by inject<RecipeImagesService>()
    route("/recipes"){
        get("/{difficulty}/{limit}"){
            val difficulty = call.parameters["difficulty"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val limit = call.parameters["limit"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val response = recipeService.findRecipesByDifficulty(limit, difficulty, recipeImagesService)
            if(response.isEmpty()){
                call.respond(HttpStatusCode.NoContent, "Recipes not found")
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}
fun Route.quickRecipes(){
    val recipeService by inject<RecipeService>()
    val recipeImagesService by inject<RecipeImagesService>()
    route("/quick-recipes"){
        get("/{limit}"){
            val limit = call.parameters["limit"]?.toInt() ?: return@get call.respond(HttpStatusCode.BadRequest)
            val response = recipeService.findQuickRecipes(limit, recipeImagesService)
            if(response.isEmpty()){
                call.respond(HttpStatusCode.NoContent, "Recipes not found")
            }
            call.respond(HttpStatusCode.OK, response)
        }
    }
}

fun Route.recipeSearch(){
    val recipeService by inject<RecipeService>()
    route("/search-recipes"){
        get(){
            val request = call.receive<RecipeSearchRequest>()
            val recipes = recipeService.searchRecipes(request)
            if(recipes.isNotEmpty()){
                call.respond(HttpStatusCode.OK, recipes)
            }
            else{
                call.respond(HttpStatusCode.NotFound, listOf<RecipeCardResponse>())
            }
        }
    }
}

fun Route.deleteRecipe(){
    val recipeService by inject<RecipeService>()
    val recipeImagesService by inject<RecipeImagesService>()
    val recipeDietsService by inject<RecipeDietsService>()
    val recipeAllergenService by inject<RecipeAllergenService>()
    val ingredientUnitService by inject<IngredientUnitService>()
    route("/delete-recipe") {
        authenticate {
            delete {
                val isAdmin = call.requireAdmin()
                if (isAdmin){
                    val id = call.receive<RecipeIDRequest>().recipeId
                    val check = mutableListOf<Boolean>()
                    check.add(recipeImagesService.deleteAllRecipeImages(id))
                    check.add(recipeDietsService.deleteAllRecipeDiets(id))
                    check.add(recipeAllergenService.deleteAllRecipeAllergens(id))
                    check.add(ingredientUnitService.deleteAllRecipeIngredients(id))
                    check.add(recipeService.deleteRecipe(id))

                    if(!check.any()){
                        call.respond(HttpStatusCode.InternalServerError, "We could not delete the recipe")
                    }
                    else{
                        call.respond(HttpStatusCode.OK, "recipe is deleted")
                    }

                }
                else{
                    call.respond(HttpStatusCode.Unauthorized)
                }
            }
        }
    }
}

fun Route.uploadRecipe() {
    val recipeService by inject<RecipeService>()
    val recipeImagesService by inject<RecipeImagesService>()
    val recipeDietsService by inject<RecipeDietsService>()
    val recipeAllergenService by inject<RecipeAllergenService>()
    val ingredientUnitService by inject<IngredientUnitService>()

//    authenticate {
//        route("/upload-recipe") {
//            post {
//                val newRecipe = call.receive<RecipeUploadRequest>()
//                val id = recipeService.createUploadedRecipe(
//                    newRecipe,
////                    recipeImagesService,
//                    recipeDietsService,
//                    recipeAllergenService,
//                    ingredientUnitService
//                )
//                call.respond(HttpStatusCode.Created, RecipeResponse(id))
//            }
//        }
//    }
    route("/update-recipe") {
        post {
            val multipart = call.receiveMultipart()
            var recipeJson: String? = null
            val imageFiles = mutableListOf<Pair<String, ByteArray>>() // (filename, bytes)

            multipart.forEachPart { part ->
                when (part) {

                    is PartData.FormItem -> {
                        if (part.name == "recipe") {
                            recipeJson = part.value
                        }
                    }

                    is PartData.FileItem -> {
                        val fileName = part.originalFileName ?: "image_${System.currentTimeMillis()}.jpg"
                        val bytes = part.streamProvider().readBytes()
                        imageFiles += fileName to bytes
                    }

                    else -> part.dispose()
                }
                part.dispose()
            }

            if (recipeJson == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing recipe JSON")
                return@post
            }

            val newRecipe = Json.decodeFromString<RecipeUploadRequest>(recipeJson!!)
            var id : Int
            if(newRecipe.recipeId != null){
                id = newRecipe.recipeId

            }
            else {
                id = recipeService.createUploadedRecipe(
                    newRecipe,
                    recipeDietsService,
                    recipeAllergenService,
                    ingredientUnitService
                )
            }
            for (uploadedImage in imageFiles) {
                if (uploadedImage.toList().isEmpty()) {
                    call.respond(HttpStatusCode.BadRequest, "No file uploaded")
                    return@post
                } else {
                    recipeImagesService.uploadImage(id, uploadedImage.first, uploadedImage.second)
                }
            }

            call.respond(HttpStatusCode.Created, RecipeResponse(id))

        }
    }
}