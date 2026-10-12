package com.smartgym.backend.service

import com.smartgym.backend.dto.AgendamentoRequest
import com.smartgym.backend.dto.AgendamentoResponse
import com.smartgym.backend.exception.NotFoundException
import com.smartgym.backend.mapper.AgendamentoMapper
import com.smartgym.backend.repository.AgendamentoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Regras de negócio (CRUD) do Agendamento.
 */
@Service
class AgendamentoService(
    private val agendamentoRepository: AgendamentoRepository,
    private val agendamentoMapper: AgendamentoMapper
) {

    @Transactional
    fun criar(request: AgendamentoRequest): AgendamentoResponse {
        val agendamento = agendamentoMapper.toEntity(request)
        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento))
    }

    @Transactional(readOnly = true)
    fun listar(): List<AgendamentoResponse> =
        agendamentoRepository.findAll().map { agendamentoMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): AgendamentoResponse =
        agendamentoMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun buscarPorTipoAula(termo: String): List<AgendamentoResponse> =
        agendamentoRepository.buscarPorTipoAulaParcial(termo).map { agendamentoMapper.toResponse(it) }

    @Transactional
    fun atualizar(id: Long, request: AgendamentoRequest): AgendamentoResponse {
        val agendamento = buscarEntidade(id)
        agendamentoMapper.updateEntity(agendamento, request)
        return agendamentoMapper.toResponse(agendamentoRepository.save(agendamento))
    }

    @Transactional
    fun remover(id: Long) {
        agendamentoRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long) =
        agendamentoRepository.findById(id).orElseThrow { NotFoundException("Agendamento $id não encontrado") }
}
