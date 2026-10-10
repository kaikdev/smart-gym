package com.smartgym.backend.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Size
import java.time.LocalDate

/**
 * Dados recebidos na requisição (POST/PUT de equipamento).
 */
data class EquipamentoRequest(

    @field:NotBlank(message = "O nome é obrigatório")
    @field:Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    val nome: String? = null,

    @field:Size(max = 50, message = "O fabricante deve ter no máximo 50 caracteres")
    val fabricante: String? = null,

    @field:PastOrPresent(message = "A data de compra não pode estar no futuro")
    val dataCompra: LocalDate? = null,

    @field:NotNull(message = "O peso suportado é obrigatório")
    @field:Min(value = 1, message = "O peso suportado deve ser no mínimo 1")
    val pesoSuportado: Double? = null,

    @field:Size(max = 50, message = "O código do sensor deve ter no máximo 50 caracteres")
    val codigoSensorIot: String? = null,

    val emUsoIot: Boolean? = false
)
