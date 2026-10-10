package com.smartgym.backend.dto

/**
 * Resposta do /api/auth/signin com o token JWT gerado.
 */
data class TokenResponse(
    val token: String,
    val tipo: String = "Bearer"
)
