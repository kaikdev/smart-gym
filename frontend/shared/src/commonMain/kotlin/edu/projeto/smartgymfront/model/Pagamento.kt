package edu.projeto.smartgymfront.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Pagamento(
    val id: Long? = null,
    val metodo: String = "",
    val codigoTransacao: String? = null,
    val dataProcessamento: LocalDateTime? = null,
    val valorFinal: Double = 0.0,
    val confirmado: Boolean = false
)
