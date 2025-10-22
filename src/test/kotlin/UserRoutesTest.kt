package api.routes

import api.models.Role
import api.models.User
import io.ktor.client.plugins.contentnegotiation.* // client plugin
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.* // server plugin
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import service.UserService
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UserRoutesTest {

    private val userService = mockk<UserService>()

    private val users = listOf(
        User(name = "Alice", password = "secret123", email = "alice@example.com", role = Role.ADMIN, id = 1L),
        User(name = "Bob", password = "hunter2", email = "bob@example.com", role = Role.USER, id = 2L)
    )

    @Test
    fun `GET all users returns OK and list`() = testApplication {
        coEvery { userService.findAll() } returns users

        application {
            this.install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
            routing {
                userRoutes(userService)
            }
        }

        val client = createClient {
            this.install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val response = client.get("/users")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("Alice"))
        assertTrue(body.contains("Bob"))
    }

    @Test
    fun `GET user by id returns user`() = testApplication {
        val mockUser = User(
            name = "Alice",
            password = "secret123",
            email = "alice@example.com",
            role = Role.ADMIN,
            id = 1L
        )
        coEvery { userService.findById(1L) } returns mockUser

        application {
            this.install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
            routing {
                userRoutes(userService)
            }
        }

        val client = createClient {
            this.install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val response = client.get("/users/1")

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("Alice"))
        assertTrue(body.contains("alice@example.com"))
    }

    @Test
    fun `GET user by id returns 404 when not found`() = testApplication {
        coEvery { userService.findById(99L) } returns null

        application {
            this.install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
            routing {
                userRoutes(userService)
            }
        }

        val client = createClient {
            this.install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val response = client.get("/users/99")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `GET user by id returns 400 for invalid id`() = testApplication {
        application {
            this.install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
            routing {
                userRoutes(userService)
            }
        }

        val client = createClient {
            this.install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val response = client.get("/users/invalid")

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}
