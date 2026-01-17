package api.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.util.reflect.TypeInfo
import org.koin.ktor.ext.inject
import service.DietsService
import service.UserDietsService

fun Route.dietRoutes() {
    val dietsService by inject<DietsService>()
    val userDietsService by inject<UserDietsService>()
    route("/getdiet") {
        get(){
            val diet = dietsService.getDietById(1)
            call.respond(HttpStatusCode.OK, diet as TypeInfo?)
        }
        get("/user-1"){
            val diets = userDietsService.getUserDietEntries("alice@example.com")
            call.respond(HttpStatusCode.OK, diets)
        }
    }
}