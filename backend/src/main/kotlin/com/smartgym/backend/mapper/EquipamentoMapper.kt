package com.smartgym.backend.mapper

import com.smartgym.backend.dto.EquipamentoRequest
import com.smartgym.backend.dto.EquipamentoResponse
import com.smartgym.backend.entity.Equipamento
import org.springframework.stereotype.Component

/**
 * Converte Equipamento <-> DTOs.
 */
@Component
class EquipamentoMapper {

    fun toEntity(request: EquipamentoRequest): Equipamento =
        Equipamento(
            nome = request.nome!!,
            fabricante = request.fabricante,
            dataCompra = request.dataCompra,
            pesoSuportado = request.pesoSuportado!!,
            codigoSensorIot = request.codigoSensorIot?.ifBlank { null },
            emUsoIot = request.emUsoIot ?: false
        )

    fun updateEntity(equipamento: Equipamento, request: EquipamentoRequest) {
        equipamento.nome = request.nome!!
        equipamento.fabricante = request.fabricante
        equipamento.dataCompra = request.dataCompra
        equipamento.pesoSuportado = request.pesoSuportado!!
        equipamento.codigoSensorIot = request.codigoSensorIot?.ifBlank { null }
        equipamento.emUsoIot = request.emUsoIot ?: false
    }

    fun toResponse(equipamento: Equipamento): EquipamentoResponse =
        EquipamentoResponse(
            id = equipamento.id,
            nome = equipamento.nome,
            fabricante = equipamento.fabricante,
            dataCompra = equipamento.dataCompra,
            pesoSuportado = equipamento.pesoSuportado,
            codigoSensorIot = equipamento.codigoSensorIot,
            emUsoIot = equipamento.emUsoIot
        )
}
