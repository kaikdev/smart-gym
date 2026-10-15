package edu.projeto.smartgymfront.model

import kotlinx.serialization.Serializable

@Serializable
data class Exercicio(
    val id: Long? = null,
    val nome: String = "",
    val grupoMuscular: String = "",
    val series: Int = 1,
    val repeticoes: Int = 1,
    val cargaSugerida: Float? = null,
    val precisaAcompanhamento: Boolean = false
)
