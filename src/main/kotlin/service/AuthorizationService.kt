package service

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import api.models.Role

class AuthorizationService {
    suspend fun hasRole(call: ApplicationCall, requiredRole: Role): Boolean {
        val principal = call.principal<JWTPrincipal>()
        val roleName = principal?.payload?.getClaim("role")?.asString()?.uppercase()
        return roleName != null && Role.valueOf(roleName) == requiredRole
        // Of: roleName == requiredRole.name
    }
    suspend fun hasAnyRole(call: ApplicationCall, vararg roles: Role): Boolean {
        val principal = call.principal<JWTPrincipal>()
        val roleName = principal?.payload?.getClaim("role")?.asString()?.uppercase()
        return roles.any { it.name == roleName }
    }
}