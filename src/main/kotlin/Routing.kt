package api

import api.repository.FakeIngredientsRepository
import api.repository.FakeRecipeRepository
import api.routes.ingredientsRoutes
import api.routes.userRoutes
import api.routes.recipesRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.TokenConfig
import routes.authenticated
import routes.getSecretInfo
import routes.signIn
import routes.signUp
import service.TokenService
import service.UserService

fun Application.configureRouting(
    tempUser : UserService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
    ) {
    routing {
        userRoutes(tempUser)
        ingredientsRoutes(FakeIngredientsRepository)
        recipesRoutes(FakeRecipeRepository)

        signUp(tempUser)
        signIn(tempUser, tokenService, tokenConfig)
        authenticated()
        getSecretInfo()
        get("/") {
            call.respondText("Hello World!")
        }
    }
}
