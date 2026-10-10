package com.smartgym.backend.service

import com.smartgym.backend.dto.PlanoRequest
import com.smartgym.backend.dto.PlanoResponse
import com.smartgym.backend.exception.NotFoundException
import com.smartgym.backend.mapper.PlanoMapper
import com.smartgym.backend.repository.PlanoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Regras de negócio (CRUD) do Plano.
 */
@Service
class PlanoService(
    private val planoRepository: PlanoRepository,
    private val planoMapper: PlanoMapper
) {

    @Transactional
    fun criar(request: PlanoRequest): PlanoResponse {
        val plano = planoMapper.toEntity(request)
        return planoMapper.toResponse(planoRepository.save(plano))
    }

    @Transactional(readOnly = true)
    fun listar(): List<PlanoResponse> =
        planoRepository.findAll().map { planoMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): PlanoResponse =
        planoMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun buscarPorDescricao(termo: String): List<PlanoResponse> =
        planoRepository.buscarPorDescricaoParcial(termo).map { planoMapper.toResponse(it) }

    @Transactional
    fun atualizar(id: Long, request: PlanoRequest): PlanoResponse {
        val plano = buscarEntidade(id)
        planoMapper.updateEntity(plano, request)
        return planoMapper.toResponse(planoRepository.save(plano))
    }

    @Transactional
    fun remover(id: Long) {
        planoRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long) =
        planoRepository.findById(id).orElseThrow { NotFoundException("Plano $id não encontrado") }
}
