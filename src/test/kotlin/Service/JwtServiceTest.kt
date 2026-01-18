package service.Service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import models.dto.TokenClaim
import models.dto.TokenConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import service.JwtService

class JwtServiceTest {
    private val jwtService = JwtService()

    private val config = TokenConfig(
        issuer = "test-issuer",
        audience = "test-audience",
        expiresIn = 60_000, // 1 minuut
        secret = "super-secret"
    )

    // JWTS-01
    @Test
    fun `generate returns a valid JWT token`() {
        val token = jwtService.generate(
            config,
            TokenClaim("userId", "123")
        )

        assertNotNull(token)
        assertTrue(token.isNotBlank())
    }

    // JWTS-02
    @Test
    fun `generated token contains userId claim`() {
        val token = jwtService.generate(
            config,
            TokenClaim("userId", "123")
        )

        val decoded = JWT.require(Algorithm.HMAC256(config.secret))
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .build()
            .verify(token)

        assertEquals("123", decoded.getClaim("userId").asString())
    }

    // JWTS-03
    @Test
    fun `generated token has expiration date`() {
        val token = jwtService.generate(
            config,
            TokenClaim("userId", "123")
        )

        val decoded = JWT.decode(token)
        assertNotNull(decoded.expiresAt)
    }

    // JWTS-04
    @Test
    fun `token verification fails with wrong secret`() {
        val token = jwtService.generate(
            config,
            TokenClaim("userId", "123")
        )

        val wrongAlgorithm = Algorithm.HMAC256("wrong-secret")

        assertThrows(Exception::class.java) {
            JWT.require(wrongAlgorithm)
                .withIssuer(config.issuer)
                .withAudience(config.audience)
                .build()
                .verify(token)
        }
    }

}