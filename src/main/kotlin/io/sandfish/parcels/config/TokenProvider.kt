package io.sandfish.parcels.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import java.util.stream.Collectors


/**
 * This class handles everything JWT
 */
@Component
class TokenProvider : Serializable {

    @Value("\${jwt.token.validity}")
    var TOKEN_VALIDITY: Long = 0

    @Value("\${jwt.signing.key}")
    lateinit var SIGNING_KEY: String

    @Value("\${jwt.authorities.key}")
    lateinit var AUTHORITIES_KEY: String

    fun getUsernameFromToken(token: String?): String {
        return getClaimFromToken(token) { obj: Claims -> obj.subject }
    }

    fun getExpirationDateFromToken(token: String?): Date {
        return getClaimFromToken(token) { obj: Claims -> obj.expiration }
    }

    fun <T> getClaimFromToken(token: String?, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.invoke(claims)
    }

    private fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser()
            .setSigningKey(SIGNING_KEY)
            .parseClaimsJws(token)
            .body
    }

    private fun isTokenExpired(token: String?): Boolean {
        val expiration: Date = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    /**
     * Build the token based on user and configuration
     */
    fun generateToken(authentication: Authentication): String {
        val authorities: String = authentication.authorities.stream()
            .map { obj: GrantedAuthority -> obj.authority }
            .collect(Collectors.joining(","))

        return Jwts.builder()
            .setSubject(authentication.name)
            .claim(AUTHORITIES_KEY, authorities)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
            .compact()
    }

    fun validateToken(token: String?, userDetails: UserDetails): Boolean {
        val username = getUsernameFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun getAuthenticationToken(
        token: String?,
        existingAuth: Authentication?,
        userDetails: UserDetails?
    ): UsernamePasswordAuthenticationToken {
        val jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY)
        val claimsJws = jwtParser.parseClaimsJws(token)
        val claims = claimsJws.body
        val authorities: Collection<GrantedAuthority> =
            Arrays.stream(claims[AUTHORITIES_KEY].toString().split(",").toTypedArray())
                .map { role: String? -> SimpleGrantedAuthority(role) }
                .collect(Collectors.toList())
        return UsernamePasswordAuthenticationToken(userDetails, "", authorities)
    }
}
