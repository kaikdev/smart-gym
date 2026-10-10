package com.smartgym.backend.service

import com.smartgym.backend.dto.EquipamentoRequest
import com.smartgym.backend.dto.EquipamentoResponse
import com.smartgym.backend.exception.ConflictException
import com.smartgym.backend.exception.NotFoundException
import com.smartgym.backend.mapper.EquipamentoMapper
import com.smartgym.backend.repository.EquipamentoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Regras de negócio (CRUD) do Equipamento.
 */
@Service
class EquipamentoService(
    private val equipamentoRepository: EquipamentoRepository,
    private val equipamentoMapper: EquipamentoMapper
) {

    @Transactional
    fun criar(request: EquipamentoRequest): EquipamentoResponse {
        verificarSensorDisponivel(request.codigoSensorIot, null)
        val equipamento = equipamentoMapper.toEntity(request)
        return equipamentoMapper.toResponse(equipamentoRepository.save(equipamento))
    }

    @Transactional(readOnly = true)
    fun listar(): List<EquipamentoResponse> =
        equipamentoRepository.findAll().map { equipamentoMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): EquipamentoResponse =
        equipamentoMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun buscarPorNome(termo: String): List<EquipamentoResponse> =
        equipamentoRepository.buscarPorNomeParcial(termo).map { equipamentoMapper.toResponse(it) }

    @Transactional
    fun atualizar(id: Long, request: EquipamentoRequest): EquipamentoResponse {
        val equipamento = buscarEntidade(id)
        verificarSensorDisponivel(request.codigoSensorIot, id)
        equipamentoMapper.updateEntity(equipamento, request)
        return equipamentoMapper.toResponse(equipamentoRepository.save(equipamento))
    }

    @Transactional
    fun remover(id: Long) {
        equipamentoRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long) =
        equipamentoRepository.findById(id).orElseThrow { NotFoundException("Equipamento $id não encontrado") }

    // Cada sensor pertence a uma única máquina
    private fun verificarSensorDisponivel(codigoSensorIot: String?, idAtual: Long?) {
        if (codigoSensorIot.isNullOrBlank()) return
        val outro = equipamentoRepository.findByCodigoSensorIot(codigoSensorIot)
        if (outro != null && outro.id != idAtual) {
            throw ConflictException("O sensor $codigoSensorIot já está vinculado ao equipamento ${outro.id}")
        }
    }
}
