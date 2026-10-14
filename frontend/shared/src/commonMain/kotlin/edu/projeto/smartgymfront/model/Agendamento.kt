package edu.projeto.smartgymfront.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Agendamento(
    val id: Long? = null,
    val tipoAula: String = "",
    val instrutor: String? = null,
    val dataHoraAgendada: LocalDateTime? = null,
    val capacidadeMaxima: Int = 1,
    val cancelado: Boolean = false
)
