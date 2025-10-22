package test

import api.models.User
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.* // client plugin
import io.ktor.client.request.*
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
import routes.signUp
import routes.signIn
import responses.AuthResponse
import requests.AuthRequest
import service.UserService
import service.TokenService
import models.TokenConfig
import kotlin.test.assertEquals

class AuthRoutesTest {

    private val mockUserService = mockk<UserService>()
    private val mockTokenService = mockk<TokenService>()
    private val tokenConfig = TokenConfig("issuer", "audience", 3600, "secret")

    @Test
    fun `POST signup should create new user`() = testApplication {
        val fakeUser = User(
            name = "Alice",
            password = "secret",
            email = "alice@example.com",
            id = 1L
        )

        // Mock: create expects api.models.User and returns a User
        coEvery { mockUserService.create(any<User>()) } returns fakeUser

        application {
            this.install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
            routing {
                signUp(mockUserService)
            }
        }

        val client = createClient {
            this.install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val request = AuthRequest(username = "Alice", password = "secret", email = "alice@example.com")
        val response = client.post("/signup") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `POST signin should return token`() = testApplication {
        val fakeUser = User(
            name = "Alice",
            password = "secret",
            email = "alice@example.com",
            id = 1L
        )

        // Mock findByUsername + token
        coEvery { mockUserService.findByUsername("Alice") } returns fakeUser
        coEvery { mockTokenService.generate(any(), any()) } returns "fake-jwt-token"

        application {
            this.install(io.ktor.server.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
            routing {
                signIn(mockUserService, mockTokenService, tokenConfig)
            }
        }

        val client = createClient {
            this.install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json { prettyPrint = true })
            }
        }

        val request = AuthRequest(username = "Alice", password = "secret", email = "alice@example.com")
        val response = client.post("/signin") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.body<AuthResponse>()
        assertEquals("fake-jwt-token", body.token)
    }
}
