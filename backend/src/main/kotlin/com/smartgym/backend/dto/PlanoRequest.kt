package com.smartgym.backend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

/**
 * Dados recebidos na requisição (POST/PUT de plano).
 */
data class PlanoRequest(

    @field:NotBlank(message = "A descrição é obrigatória")
    @field:Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres")
    val descricao: String? = null,

    @field:NotNull(message = "O valor mensal é obrigatório")
    @field:Positive(message = "O valor mensal deve ser maior que zero")
    val valorMensal: Double? = null,

    @field:PositiveOrZero(message = "Os meses de fidelidade não podem ser negativos")
    val mesesFidelidade: Int? = 0,

    @field:PositiveOrZero(message = "A taxa de matrícula não pode ser negativa")
    val taxaMatricula: Double? = 0.0,

    val promocional: Boolean? = false
)
