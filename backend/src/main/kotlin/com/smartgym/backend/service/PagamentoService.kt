package com.smartgym.backend.service

import com.smartgym.backend.dto.PagamentoRequest
import com.smartgym.backend.dto.PagamentoResponse
import com.smartgym.backend.exception.ConflictException
import com.smartgym.backend.exception.NotFoundException
import com.smartgym.backend.mapper.PagamentoMapper
import com.smartgym.backend.repository.PagamentoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Regras de negócio (CRUD) do Pagamento.
 */
@Service
class PagamentoService(
    private val pagamentoRepository: PagamentoRepository,
    private val pagamentoMapper: PagamentoMapper
) {

    @Transactional
    fun criar(request: PagamentoRequest): PagamentoResponse {
        verificarCodigoDisponivel(request.codigoTransacao, null)
        val pagamento = pagamentoMapper.toEntity(request)
        return pagamentoMapper.toResponse(pagamentoRepository.save(pagamento))
    }

    @Transactional(readOnly = true)
    fun listar(): List<PagamentoResponse> =
        pagamentoRepository.findAll().map { pagamentoMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): PagamentoResponse =
        pagamentoMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun buscarPorMetodo(termo: String): List<PagamentoResponse> =
        pagamentoRepository.buscarPorMetodoParcial(termo).map { pagamentoMapper.toResponse(it) }

    @Transactional
    fun atualizar(id: Long, request: PagamentoRequest): PagamentoResponse {
        val pagamento = buscarEntidade(id)
        verificarCodigoDisponivel(request.codigoTransacao, id)
        pagamentoMapper.updateEntity(pagamento, request)
        return pagamentoMapper.toResponse(pagamentoRepository.save(pagamento))
    }

    @Transactional
    fun remover(id: Long) {
        pagamentoRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long) =
        pagamentoRepository.findById(id).orElseThrow { NotFoundException("Pagamento $id não encontrado") }

    // Cada código de transação pertence a um único pagamento
    private fun verificarCodigoDisponivel(codigoTransacao: String?, idAtual: Long?) {
        if (codigoTransacao.isNullOrBlank()) return
        val outro = pagamentoRepository.findByCodigoTransacao(codigoTransacao)
        if (outro != null && outro.id != idAtual) {
            throw ConflictException("O código de transação $codigoTransacao já está registrado no pagamento ${outro.id}")
        }
    }
}
