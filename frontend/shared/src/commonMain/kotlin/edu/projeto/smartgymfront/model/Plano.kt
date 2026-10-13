package edu.projeto.smartgymfront.model

import kotlinx.serialization.Serializable

@Serializable
data class Plano(
    val id: Long? = null,
    val descricao: String = "",
    val valorMensal: Double = 0.0,
    val mesesFidelidade: Int = 0,
    val taxaMatricula: Double = 0.0,
    val promocional: Boolean = false
)
