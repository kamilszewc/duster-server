package pl.integrable.dusterserver.service

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.ArrayList
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter(private val tokenService: JwtTokenService) : OncePerRequestFilter() {
    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = httpServletRequest.getHeader("Authorization")
        if (authorizationHeaderIsInvalid(authorizationHeader)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse)
            return
        }
        val token = createToken(authorizationHeader)
        SecurityContextHolder.getContext().authentication = token
        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }

    private fun authorizationHeaderIsInvalid(authorizationHeader: String?): Boolean {
        return (authorizationHeader == null
                || !authorizationHeader.startsWith("Bearer "))
    }

    private fun createToken(authorizationHeader: String): UsernamePasswordAuthenticationToken {
        val token = authorizationHeader.replace("Bearer ", "")
        val jwtTokenPrincipal: JwtTokenPrincipal = tokenService.parseToken(token)
        val authorities: MutableList<GrantedAuthority> = ArrayList()
        authorities.add(SimpleGrantedAuthority(jwtTokenPrincipal.role))
        return UsernamePasswordAuthenticationToken(jwtTokenPrincipal, null, authorities)
    }
}