package pl.integrable.dusterserver.service

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import pl.integrable.dusterserver.model.Sensor
import pl.integrable.dusterserver.property.CredentialsProperties
import java.security.Key
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class JwtTokenService @Autowired constructor(credentialsProperties: CredentialsProperties) {
    private val JWT_SECRET = credentialsProperties.tokenSecret

    fun generateToken(sensor: Sensor): String {
        val expirationTime = Instant.now().plus(100000, ChronoUnit.SECONDS)
        val expirationDate = Date.from(expirationTime)
        val key: Key = Keys.hmacShaKeyFor(JWT_SECRET.toByteArray())
        return Jwts.builder()
            .claim("id", sensor.id)
            .claim("sub", sensor.name)
            .claim("role", "ROLE_SENSOR")
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
        val name = jwsClaims.body.subject
        val role = jwsClaims.body.get("role", String::class.java)
        val id = jwsClaims.body.get("id", Integer::class.java)
        return JwtTokenPrincipal(id, name, role)
    }
}
