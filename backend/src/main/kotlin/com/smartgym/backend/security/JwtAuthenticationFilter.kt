package com.smartgym.backend.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

/**
 * Filtro executado em toda requisição: lê o cabeçalho "Authorization: Bearer <token>",
 * valida o token e, se estiver tudo certo, marca o usuário como autenticado.
 */
@Component
class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val cabecalho = request.getHeader("Authorization")

        if (cabecalho != null && cabecalho.startsWith("Bearer ")) {
            val token = cabecalho.substring(7)

            if (jwtUtil.validarToken(token) && SecurityContextHolder.getContext().authentication == null) {
                val email = jwtUtil.obterEmail(token)
                val usuario = userDetailsService.loadUserByUsername(email)

                if (usuario.isEnabled) {
                    val autenticacao = UsernamePasswordAuthenticationToken(usuario, null, usuario.authorities)
                    autenticacao.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = autenticacao
                }
            }
        }

        filterChain.doFilter(request, response)
    }
}
