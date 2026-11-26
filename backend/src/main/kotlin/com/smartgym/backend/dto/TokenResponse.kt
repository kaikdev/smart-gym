package com.smartgym.backend.dto

data class TokenResponse(
    val token: String,
    val perfil: String,
    val tipo: String = "Bearer"
)
