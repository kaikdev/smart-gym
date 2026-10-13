package edu.projeto.smartgymfront.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class AvaliacaoFisica(
    val id: Long? = null,
    val avaliador: String = "",
    val dataAvaliacao: LocalDate? = null,
    val altura: Float = 0f,
    val imc: Float = 0f,
    val percentualGordura: Double? = null
)
