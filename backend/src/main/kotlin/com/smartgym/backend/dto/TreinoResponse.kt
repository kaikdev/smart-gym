package com.smartgym.backend.dto

import java.time.LocalDate

/**
 * Dados do treino devolvidos na resposta.
 */
data class TreinoResponse(
    val id: Long?,
    val titulo: String,
    val objetivo: String?,
    val dataCriacao: LocalDate,
    val duracaoEstimadaMin: Int,
    val publico: Boolean
)
