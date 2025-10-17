package api.service

import api.models.Role
import api.models.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JwtTokenProvider(private val secret: String, private val issuer: String, private val expiresMs: Long) : TokenProvider {
    val roles: Set<Role> = setOf(Role.USER, Role.ADMIN)
    val rolesArray: Array<String> = roles.map { it.name }.toTypedArray()
    private val algorithm = Algorithm.HMAC256(secret)
    override fun generateToken(user: User): String {
        val now = System.currentTimeMillis()
        return JWT.create()
            .withIssuer(issuer)
            .withSubject(user.id.toString())
            .withClaim("email", user.email)
            .withArrayClaim("roles", rolesArray)
            .withIssuedAt(Date(now))
            .withExpiresAt(Date(now + expiresMs))
            .sign(algorithm)
    }

    override fun validateToken(token: String): Long? {
        val verifier = JWT.require(algorithm).withIssuer(issuer).build()
        val decoded = verifier.verify(token)
        val rolesClaim = decoded.getClaim("roles")?.asList(String::class.java) ?: emptyList()
        val roles = rolesClaim.mapNotNull { name -> try { Role.valueOf(name) } catch (_: IllegalArgumentException) { null } }.toSet()

//        val decoded = verifier.verify(token)
        return decoded.subject?.toLong()
    }
}
