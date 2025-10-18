package service

import models.TokenClaim
import models.TokenConfig

interface TokenService {
    fun generate(
        config: TokenConfig,
        vararg claims: TokenClaim
    ) : String
}