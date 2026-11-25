package com.smartgym.backend.service

import com.smartgym.backend.dto.LeituraSensorResponse
import com.smartgym.backend.entity.LeituraSensor
import com.smartgym.backend.mapper.LeituraSensorMapper
import com.smartgym.backend.repository.EquipamentoRepository
import com.smartgym.backend.repository.LeituraSensorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LeituraSensorService(
    private val leituraSensorRepository: LeituraSensorRepository,
    private val equipamentoRepository: EquipamentoRepository,
    private val leituraSensorMapper: LeituraSensorMapper
) {

    @Transactional
    fun registrar(codigoSensor: String, emUso: Boolean): LeituraSensorResponse {
        val equipamento = equipamentoRepository.findByCodigoSensorIot(codigoSensor)
        if (equipamento != null && equipamento.emUsoIot != emUso) {
            equipamento.emUsoIot = emUso
            equipamentoRepository.save(equipamento)
        }
        val leitura = LeituraSensor(
            codigoSensor = codigoSensor,
            emUso = emUso,
            equipamentoId = equipamento?.id
        )
        return leituraSensorMapper.toResponse(leituraSensorRepository.save(leitura))
    }

    @Transactional(readOnly = true)
    fun listar(): List<LeituraSensorResponse> =
        leituraSensorRepository.findTop100ByOrderByDataHoraLeituraDesc().map { leituraSensorMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun listarPorSensor(codigoSensor: String): List<LeituraSensorResponse> =
        leituraSensorRepository.findTop100ByCodigoSensorOrderByDataHoraLeituraDesc(codigoSensor)
            .map { leituraSensorMapper.toResponse(it) }
}
