package com.smartgym.backend.dto

import java.time.LocalDate

/**
 * Dados da avaliação física devolvidos na resposta.
 */
data class AvaliacaoFisicaResponse(
    val id: Long?,
    val avaliador: String,
    val dataAvaliacao: LocalDate,
    val altura: Float,
    val imc: Float,
    val percentualGordura: Double?
)
