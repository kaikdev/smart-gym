package com.smartgym.backend.service

import com.smartgym.backend.dto.TreinoRequest
import com.smartgym.backend.dto.TreinoResponse
import com.smartgym.backend.exception.NotFoundException
import com.smartgym.backend.mapper.TreinoMapper
import com.smartgym.backend.repository.TreinoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Regras de negócio (CRUD) do Treino.
 */
@Service
class TreinoService(
    private val treinoRepository: TreinoRepository,
    private val treinoMapper: TreinoMapper
) {

    @Transactional
    fun criar(request: TreinoRequest): TreinoResponse {
        val treino = treinoMapper.toEntity(request)
        return treinoMapper.toResponse(treinoRepository.save(treino))
    }

    @Transactional(readOnly = true)
    fun listar(): List<TreinoResponse> =
        treinoRepository.findAll().map { treinoMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): TreinoResponse =
        treinoMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun buscarPorTitulo(termo: String): List<TreinoResponse> =
        treinoRepository.buscarPorTituloParcial(termo).map { treinoMapper.toResponse(it) }

    @Transactional
    fun atualizar(id: Long, request: TreinoRequest): TreinoResponse {
        val treino = buscarEntidade(id)
        treinoMapper.updateEntity(treino, request)
        return treinoMapper.toResponse(treinoRepository.save(treino))
    }

    @Transactional
    fun remover(id: Long) {
        treinoRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long) =
        treinoRepository.findById(id).orElseThrow { NotFoundException("Treino $id não encontrado") }
}
