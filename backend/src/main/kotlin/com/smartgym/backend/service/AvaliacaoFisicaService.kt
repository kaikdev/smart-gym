package com.smartgym.backend.service

import com.smartgym.backend.dto.AvaliacaoFisicaRequest
import com.smartgym.backend.dto.AvaliacaoFisicaResponse
import com.smartgym.backend.exception.NotFoundException
import com.smartgym.backend.mapper.AvaliacaoFisicaMapper
import com.smartgym.backend.repository.AvaliacaoFisicaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Regras de negócio (CRUD) da AvaliacaoFisica.
 */
@Service
class AvaliacaoFisicaService(
    private val avaliacaoFisicaRepository: AvaliacaoFisicaRepository,
    private val avaliacaoFisicaMapper: AvaliacaoFisicaMapper
) {

    @Transactional
    fun criar(request: AvaliacaoFisicaRequest): AvaliacaoFisicaResponse {
        val avaliacao = avaliacaoFisicaMapper.toEntity(request)
        return avaliacaoFisicaMapper.toResponse(avaliacaoFisicaRepository.save(avaliacao))
    }

    @Transactional(readOnly = true)
    fun listar(): List<AvaliacaoFisicaResponse> =
        avaliacaoFisicaRepository.findAll().map { avaliacaoFisicaMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): AvaliacaoFisicaResponse =
        avaliacaoFisicaMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun buscarPorAvaliador(termo: String): List<AvaliacaoFisicaResponse> =
        avaliacaoFisicaRepository.buscarPorAvaliadorParcial(termo).map { avaliacaoFisicaMapper.toResponse(it) }

    @Transactional
    fun atualizar(id: Long, request: AvaliacaoFisicaRequest): AvaliacaoFisicaResponse {
        val avaliacao = buscarEntidade(id)
        avaliacaoFisicaMapper.updateEntity(avaliacao, request)
        return avaliacaoFisicaMapper.toResponse(avaliacaoFisicaRepository.save(avaliacao))
    }

    @Transactional
    fun remover(id: Long) {
        avaliacaoFisicaRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long) =
        avaliacaoFisicaRepository.findById(id).orElseThrow { NotFoundException("Avaliação física $id não encontrada") }
}
