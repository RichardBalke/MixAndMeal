package api

import api.routes.deleteRecipe
import api.routes.dietRoutes
import api.routes.featuredRecipeDetails
import api.routes.fullRecipe
import api.routes.ingredientsRoutes
import api.routes.popularRecipes
import api.routes.quickRecipes
import api.routes.recipeCardsByDifficulty
import api.routes.recipeSearch
import api.routes.userRoutes
import api.routes.uploadRecipe
import api.routes.userFridgeRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import models.dto.TokenConfig
import routes.authenticated
import routes.getSecretInfo
import routes.signIn
import routes.signUp
import routes.userAllergensRoutes
import routes.userDietsRoutes
import routes.userFavouritesRoutes
import service.JwtService

fun Application.configureRouting(
    tokenService: JwtService,
    tokenConfig: TokenConfig
    ) {
    routing {
        // Recipes
        featuredRecipeDetails()
        fullRecipe()
        popularRecipes()
        recipeCardsByDifficulty()
        uploadRecipe()
        quickRecipes()
        recipeSearch()
        deleteRecipe()

        // Users
        userRoutes()
        userFridgeRoutes()
        signUp(tokenService, tokenConfig)
        signIn(tokenService, tokenConfig)
        authenticated()
        getSecretInfo()
        userDietsRoutes()
        userFavouritesRoutes()
        userAllergensRoutes()

        // Ingredients
        ingredientsRoutes()

        // Diets
        dietRoutes()
    }
}
