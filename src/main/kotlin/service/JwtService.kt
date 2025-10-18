package service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import models.TokenClaim
import models.TokenConfig
import java.util.Date

// stateless class that only provides functionality
class JwtService: TokenService {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        val token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis()+config.expiresIn))
        claims.forEach { claim ->
            token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}