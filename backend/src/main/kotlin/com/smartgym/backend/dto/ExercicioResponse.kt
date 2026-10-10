package com.smartgym.backend.dto

/**
 * Dados do exercício devolvidos na resposta.
 */
data class ExercicioResponse(
    val id: Long?,
    val nome: String,
    val grupoMuscular: String,
    val series: Int,
    val repeticoes: Int,
    val cargaSugerida: Float?,
    val precisaAcompanhamento: Boolean
)
