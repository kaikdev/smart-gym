package com.smartgym.backend.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import jakarta.validation.constraints.Size

/**
 * Dados recebidos na requisição (POST/PUT de exercício).
 */
data class ExercicioRequest(

    @field:NotBlank(message = "O nome é obrigatório")
    @field:Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    val nome: String? = null,

    @field:NotBlank(message = "O grupo muscular é obrigatório")
    @field:Size(max = 50, message = "O grupo muscular deve ter no máximo 50 caracteres")
    val grupoMuscular: String? = null,

    @field:NotNull(message = "O número de séries é obrigatório")
    @field:Min(value = 1, message = "O exercício deve ter no mínimo 1 série")
    val series: Int? = null,

    @field:NotNull(message = "O número de repetições é obrigatório")
    @field:Min(value = 1, message = "O exercício deve ter no mínimo 1 repetição")
    val repeticoes: Int? = null,

    @field:PositiveOrZero(message = "A carga sugerida não pode ser negativa")
    val cargaSugerida: Float? = null,

    val precisaAcompanhamento: Boolean? = false
)
