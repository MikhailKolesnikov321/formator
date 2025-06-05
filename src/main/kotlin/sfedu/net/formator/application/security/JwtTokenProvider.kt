package sfedu.net.formator.application.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import sfedu.net.formator.domain.Role
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.lifeTime}")
    private val lifeTime: Long
) {
    private lateinit var secretKey: SecretKey

    @PostConstruct
    fun init() {
        secretKey = Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun generateToken(id: String, role: Role): String {
        val now = Date()
        val expiry = Date(now.time + lifeTime)
        require(this::secretKey.isInitialized) { "SecretKey not initialized" }
        return Jwts.builder()
            .subject(id)
            .claim("role", role.name)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact()
            .let { "Bearer $it" }
    }

    fun isTokenValid(token: String): Boolean {
        return !parseClaims(token).expiration.before(Date());
    }

    fun extractEmail(token: String): String {
        return parseClaims(token).subject
    }

    fun extractId(token: String): String {
        return parseClaims(token).subject
    }

    fun extractRole(token: String): Role {
        val claims = parseClaims(token)
        val roleStr = claims.get("role", String::class.java)
        return Role.valueOf(roleStr)
    }

    private fun parseClaims(token: String) = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token.removePrefix("Bearer "))
        .payload
}