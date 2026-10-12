package com.smartgym.backend.dto

import java.time.LocalDateTime

/**
 * Dados do agendamento devolvidos na resposta.
 */
data class AgendamentoResponse(
    val id: Long?,
    val tipoAula: String,
    val instrutor: String?,
    val dataHoraAgendada: LocalDateTime,
    val capacidadeMaxima: Int,
    val cancelado: Boolean
)
