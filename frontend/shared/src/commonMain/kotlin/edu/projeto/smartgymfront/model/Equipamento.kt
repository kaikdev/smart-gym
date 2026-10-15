package edu.projeto.smartgymfront.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Equipamento(
    val id: Long? = null,
    val nome: String = "",
    val fabricante: String? = null,
    val dataCompra: LocalDate? = null,
    val pesoSuportado: Double = 1.0,
    val codigoSensorIot: String? = null,
    val emUsoIot: Boolean = false
)
