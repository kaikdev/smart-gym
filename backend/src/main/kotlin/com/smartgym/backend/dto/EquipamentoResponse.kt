package com.smartgym.backend.dto

import java.time.LocalDate

/**
 * Dados do equipamento devolvidos na resposta.
 */
data class EquipamentoResponse(
    val id: Long?,
    val nome: String,
    val fabricante: String?,
    val dataCompra: LocalDate?,
    val pesoSuportado: Double,
    val codigoSensorIot: String?,
    val emUsoIot: Boolean
)
