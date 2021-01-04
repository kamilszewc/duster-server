package pl.integrable.dusterserver.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class JwtTokenService {
    private val JWT_SECRET = "dfgij038ucQbrwzetEETZVTrO#%ASCWA#rwe3f23r4f43sgdf@"
    private val VALIDITY_HOURS = 87600 // Ten years
//    fun generateToken(user: User, validityTime: Long?): String {
//        val expirationTime = Instant.now().plus(validityTime!!, ChronoUnit.SECONDS)
//        val expirationDate = Date.from(expirationTime)
//        val key: Key = Keys.hmacShaKeyFor(JWT_SECRET.toByteArray())
//        var role = "ROLE_USER"
//        if (user.isAdmin()) {
//            role = "ROLE_ADMIN"
//        }
//        return Jwts.builder()
//            .claim("id", user.getId())
//            .claim("sub", user.getUsername())
//            .claim("role", role)
//            .setExpiration(expirationDate)
//            .signWith(key, SignatureAlgorithm.HS256)
//            .compact()
//    }

    fun generateToken(serviceName: String?, role: String?): String {
        val expirationTime = Instant.now().plus(VALIDITY_HOURS.toLong(), ChronoUnit.HOURS)
        val expirationDate = Date.from(expirationTime)
        val key: Key = Keys.hmacShaKeyFor(JWT_SECRET.toByteArray())
        return Jwts.builder()
            .claim("id", null)
            .claim("sub", serviceName)
            .claim("role", role)
            .setExpiration(expirationDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun parseToken(token: String?): JwtTokenPrincipal {
        val secretBytes = JWT_SECRET.toByteArray()
        val jwsClaims = Jwts.parserBuilder()
            .setSigningKey(secretBytes)
            .build()
            .parseClaimsJws(token)
        val name = jwsClaims.body
            .subject
        val role = jwsClaims.body
            .get("role", String::class.java)
        val id = jwsClaims.body
            .get("id", Long::class.java)
        return JwtTokenPrincipal(id, name, role)
    }
}
