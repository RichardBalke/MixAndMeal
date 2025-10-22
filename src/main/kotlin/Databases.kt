package api

import io.ktor.server.application.*

fun Application.configureDatabases() {
    // 🔹 Voor nu geen database connectie nodig in de tests
    // Als je later weer een echte DB wilt gebruiken, kun je onderstaande code terugzetten.

    /*
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = "",
    )
    val userService = UserService(database)
    routing {
        // Create user
        post("/users") {
            val user = call.receive<ExposedUser>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }

        // Read user
        get("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        // Update user
        put("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<ExposedUser>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }

        // Delete user
        delete("/{id}") {
            val id = call.parameters["id"]?.toLong() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }
    }
    */
}
