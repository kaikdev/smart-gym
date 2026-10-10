package com.smartgym.backend.dto

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PastOrPresent
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.time.LocalDate

/**
 * Dados recebidos na requisição (POST/PUT de avaliação física).
 */
data class AvaliacaoFisicaRequest(

    @field:NotBlank(message = "O avaliador é obrigatório")
    @field:Size(max = 100, message = "O nome do avaliador deve ter no máximo 100 caracteres")
    val avaliador: String? = null,

    @field:NotNull(message = "A data da avaliação é obrigatória")
    @field:PastOrPresent(message = "A data da avaliação não pode estar no futuro")
    val dataAvaliacao: LocalDate? = null,

    @field:NotNull(message = "A altura é obrigatória")
    @field:Positive(message = "A altura deve ser maior que zero")
    val altura: Float? = null,

    @field:NotNull(message = "O IMC é obrigatório")
    @field:Positive(message = "O IMC deve ser maior que zero")
    val imc: Float? = null,

    @field:DecimalMin(value = "0.0", message = "O percentual de gordura deve ser no mínimo 0")
    @field:DecimalMax(value = "100.0", message = "O percentual de gordura deve ser no máximo 100")
    val percentualGordura: Double? = null
)
