package edu.projeto.smartgymfront.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Treino(
    val id: Long? = null,
    val titulo: String = "",
    val objetivo: String? = null,
    val dataCriacao: LocalDate? = null,
    val duracaoEstimadaMin: Int = 1,
    val publico: Boolean = false
)
