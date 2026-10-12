package com.smartgym.backend.mapper

import com.smartgym.backend.dto.AgendamentoRequest
import com.smartgym.backend.dto.AgendamentoResponse
import com.smartgym.backend.entity.Agendamento
import org.springframework.stereotype.Component

/**
 * Converte Agendamento <-> DTOs.
 */
@Component
class AgendamentoMapper {

    fun toEntity(request: AgendamentoRequest): Agendamento =
        Agendamento(
            tipoAula = request.tipoAula!!,
            instrutor = request.instrutor,
            dataHoraAgendada = request.dataHoraAgendada!!,
            capacidadeMaxima = request.capacidadeMaxima!!,
            cancelado = request.cancelado ?: false
        )

    fun updateEntity(agendamento: Agendamento, request: AgendamentoRequest) {
        agendamento.tipoAula = request.tipoAula!!
        agendamento.instrutor = request.instrutor
        agendamento.dataHoraAgendada = request.dataHoraAgendada!!
        agendamento.capacidadeMaxima = request.capacidadeMaxima!!
        agendamento.cancelado = request.cancelado ?: false
    }

    fun toResponse(agendamento: Agendamento): AgendamentoResponse =
        AgendamentoResponse(
            id = agendamento.id,
            tipoAula = agendamento.tipoAula,
            instrutor = agendamento.instrutor,
            dataHoraAgendada = agendamento.dataHoraAgendada,
            capacidadeMaxima = agendamento.capacidadeMaxima,
            cancelado = agendamento.cancelado
        )
}
