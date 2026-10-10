package com.smartgym.backend.dto

import java.time.LocalDateTime

/**
 * Formato padrão das respostas de erro da API.
 */
data class ErroResponse(
    val status: Int,
    val mensagem: String,
    val erros: Map<String, String> = emptyMap(),
    val dataHora: LocalDateTime = LocalDateTime.now()
)
