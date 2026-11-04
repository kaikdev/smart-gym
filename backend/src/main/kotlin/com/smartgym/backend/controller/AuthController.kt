package com.smartgym.backend.controller

import com.smartgym.backend.dto.LoginRequest
import com.smartgym.backend.dto.TokenResponse
import com.smartgym.backend.dto.UsuarioRequest
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

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JwtUtil,
    private val usuarioService: UsuarioService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): TokenResponse =
        TokenResponse(autenticar(request.email!!, request.senha!!))

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: UsuarioRequest): ResponseEntity<TokenResponse> {
        usuarioService.criar(request)
        val token = autenticar(request.email!!, request.senha!!)
        return ResponseEntity.status(HttpStatus.CREATED).body(TokenResponse(token))
    }

    private fun autenticar(email: String, senha: String): String {
        val autenticacao = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(email, senha)
        )
        return jwtUtil.gerarToken(autenticacao.name)
    }
}
