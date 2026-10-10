package com.smartgym.backend.dto

import java.time.LocalDate

/**
 * Dados devolvidos na resposta. A senha nunca é devolvida.
 */
data class UsuarioResponse(
    val id: Long?,
    val nome: String,
    val email: String,
    val dataNascimento: LocalDate?,
    val pesoAtual: Float?,
    val ativo: Boolean
)
