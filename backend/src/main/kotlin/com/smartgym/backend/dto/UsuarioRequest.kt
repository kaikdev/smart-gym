package com.smartgym.backend.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDate

/**
 * Dados recebidos na requisição (POST/PUT de usuário e /signup).
 */
data class UsuarioRequest(

    @field:NotBlank(message = "O nome é obrigatório")
    @field:Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    val nome: String? = null,

    @field:NotBlank(message = "O e-mail é obrigatório")
    @field:Email(message = "E-mail inválido")
    @field:Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres")
    val email: String? = null,

    @field:NotBlank(message = "A senha é obrigatória")
    @field:Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres")
    val senha: String? = null,

    @field:Past(message = "A data de nascimento deve estar no passado")
    val dataNascimento: LocalDate? = null,

    @field:Positive(message = "O peso deve ser maior que zero")
    val pesoAtual: Float? = null,

    val ativo: Boolean? = true
)
