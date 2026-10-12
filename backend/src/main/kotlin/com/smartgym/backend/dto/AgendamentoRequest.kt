package com.smartgym.backend.dto

import jakarta.validation.constraints.FutureOrPresent
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

/**
 * Dados recebidos na requisição (POST/PUT de agendamento).
 */
data class AgendamentoRequest(

    @field:NotBlank(message = "O tipo de aula é obrigatório")
    @field:Size(max = 60, message = "O tipo de aula deve ter no máximo 60 caracteres")
    val tipoAula: String? = null,

    @field:Size(max = 100, message = "O nome do instrutor deve ter no máximo 100 caracteres")
    val instrutor: String? = null,

    @field:NotNull(message = "A data e hora do agendamento são obrigatórias")
    @field:FutureOrPresent(message = "O agendamento não pode ser marcado no passado")
    val dataHoraAgendada: LocalDateTime? = null,

    @field:NotNull(message = "A capacidade máxima é obrigatória")
    @field:Min(value = 1, message = "A capacidade máxima deve ser de no mínimo 1 pessoa")
    val capacidadeMaxima: Int? = null,

    val cancelado: Boolean? = false
)
