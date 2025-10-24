package service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import models.TokenClaim
import models.TokenConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JwtServiceTest {

    private val jwtService = JwtService()

    @Test
    fun `generate should include issuer audience expiry and claims`() {
        val config = TokenConfig(
            issuer = "mixandmeal-test-issuer",
            audience = "mixandmeal-test-audience",
            expiresIn = 5_000, // 5 seconds
            secret = "super-secret-test-key"
        )
        val now = System.currentTimeMillis()

        val token = jwtService.generate(
            config,
            TokenClaim("username", "alice"),
            TokenClaim("role", "user")
        )

        // Verify signature and standard claims using the same secret
        val verifier = JWT.require(Algorithm.HMAC256(config.secret))
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .build()

        val decoded = verifier.verify(token)

        // Audience and issuer already verified by the verifier; check exp window and custom claims
        val expiresAt = decoded.expiresAt
        assertNotNull(expiresAt, "Token should have an expiration time")
        val expMillis = expiresAt.time
        assertTrue(expMillis > now, "Expiration should be in the future")
        assertTrue(
            expMillis <= now + config.expiresIn + 1000, // allow 1s slack for test runtime
            "Expiration should be within configured window"
        )

        assertEquals("alice", decoded.getClaim("username").asString())
        assertEquals("user", decoded.getClaim("role").asString())
    }

    @Test
    fun `token signed with different secret should fail verification`() {
        val config = TokenConfig(
            issuer = "issuer",
            audience = "aud",
            expiresIn = 1_000,
            secret = "correct-secret"
        )

        val token = jwtService.generate(config, TokenClaim("k", "v"))

        val wrongVerifier = JWT.require(Algorithm.HMAC256("wrong-secret"))
            .withIssuer(config.issuer)
            .withAudience(config.audience)
            .build()

        assertThrows(com.auth0.jwt.exceptions.SignatureVerificationException::class.java) {
            wrongVerifier.verify(token)
        }
    }
}