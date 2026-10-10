package com.smartgym.backend.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * Dados recebidos na requisição (POST/PUT de treino).
 * A data de criação não é enviada: o sistema preenche com a data do cadastro.
 */
data class TreinoRequest(

    @field:NotBlank(message = "O título é obrigatório")
    @field:Size(max = 80, message = "O título deve ter no máximo 80 caracteres")
    val titulo: String? = null,

    @field:Size(max = 100, message = "O objetivo deve ter no máximo 100 caracteres")
    val objetivo: String? = null,

    @field:NotNull(message = "A duração estimada é obrigatória")
    @field:Min(value = 1, message = "A duração estimada deve ser de no mínimo 1 minuto")
    val duracaoEstimadaMin: Int? = null,

    val publico: Boolean? = false
)
