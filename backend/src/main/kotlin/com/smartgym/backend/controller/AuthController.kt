package com.smartgym.backend.controller

import com.smartgym.backend.dto.SigninRequest
import com.smartgym.backend.dto.TokenResponse
import com.smartgym.backend.dto.UsuarioRequest
import com.smartgym.backend.dto.UsuarioResponse
import com.smartgym.backend.security.JwtUtil
import com.smartgym.backend.service.UsuarioService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Rotas públicas de autenticação.
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val usuarioService: UsuarioService
) {

    // POST /api/auth/signup -> cadastra um novo usuário
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: UsuarioRequest): ResponseEntity<UsuarioResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(request))

    // POST /api/auth/signin -> valida e-mail e senha pelo Spring Security e devolve o token JWT
    @PostMapping("/signin")
    fun signin(@Valid @RequestBody request: SigninRequest): TokenResponse {
        val autenticacao = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.senha)
        )
        val token = jwtUtil.gerarToken(autenticacao.name)
        return TokenResponse(token)
    }
}
