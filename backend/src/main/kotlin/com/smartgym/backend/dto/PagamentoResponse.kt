package com.smartgym.backend.dto

import java.time.LocalDateTime

/**
 * Dados do pagamento devolvidos na resposta.
 */
data class PagamentoResponse(
    val id: Long?,
    val metodo: String,
    val codigoTransacao: String?,
    val dataProcessamento: LocalDateTime,
    val valorFinal: Double,
    val confirmado: Boolean
)
