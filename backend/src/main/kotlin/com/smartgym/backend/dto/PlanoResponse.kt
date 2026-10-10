package com.smartgym.backend.dto

/**
 * Dados do plano devolvidos na resposta.
 */
data class PlanoResponse(
    val id: Long?,
    val descricao: String,
    val valorMensal: Double,
    val mesesFidelidade: Int,
    val taxaMatricula: Double,
    val promocional: Boolean
)
