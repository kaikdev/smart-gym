package com.smartgym.backend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * Credenciais enviadas para /api/auth/signin.
 */
data class SigninRequest(

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "E-mail inválido")
    val email: String? = null,

    @field:NotBlank(message = "A senha é obrigatória")
    val senha: String? = null
)
