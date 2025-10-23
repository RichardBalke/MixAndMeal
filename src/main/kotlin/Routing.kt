package api

import api.repository.FakeIngredientsRepository
import api.routes.ingredientsRoutes
import api.routes.userRoutes
import api.routes.recipesRoutes
import api.service.IngredientService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.TokenConfig
import routes.authenticated
import routes.getSecretInfo
import routes.signIn
import routes.signUp
import service.JwtService
import service.RecipeService
import service.TokenService
import service.UserService

fun Application.configureRouting(
    tempUser : UserService,
    tokenService: JwtService,
    tokenConfig: TokenConfig
    ) {
    routing {
        userRoutes(tempUser)
        ingredientsRoutes(IngredientService())
        recipesRoutes(RecipeService())

        signUp(tempUser)
        signIn(tempUser, tokenService, tokenConfig)
        authenticated()
        getSecretInfo()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
