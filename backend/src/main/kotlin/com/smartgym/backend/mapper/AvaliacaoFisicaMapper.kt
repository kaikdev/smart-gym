package com.smartgym.backend.mapper

import com.smartgym.backend.dto.AvaliacaoFisicaRequest
import com.smartgym.backend.dto.AvaliacaoFisicaResponse
import com.smartgym.backend.entity.AvaliacaoFisica
import org.springframework.stereotype.Component

/**
 * Converte AvaliacaoFisica <-> DTOs.
 */
@Component
class AvaliacaoFisicaMapper {

    fun toEntity(request: AvaliacaoFisicaRequest): AvaliacaoFisica =
        AvaliacaoFisica(
            avaliador = request.avaliador!!,
            dataAvaliacao = request.dataAvaliacao!!,
            altura = request.altura!!,
            imc = request.imc!!,
            percentualGordura = request.percentualGordura
        )

    fun updateEntity(avaliacao: AvaliacaoFisica, request: AvaliacaoFisicaRequest) {
        avaliacao.avaliador = request.avaliador!!
        avaliacao.dataAvaliacao = request.dataAvaliacao!!
        avaliacao.altura = request.altura!!
        avaliacao.imc = request.imc!!
        avaliacao.percentualGordura = request.percentualGordura
    }

    fun toResponse(avaliacao: AvaliacaoFisica): AvaliacaoFisicaResponse =
        AvaliacaoFisicaResponse(
            id = avaliacao.id,
            avaliador = avaliacao.avaliador,
            dataAvaliacao = avaliacao.dataAvaliacao,
            altura = avaliacao.altura,
            imc = avaliacao.imc,
            percentualGordura = avaliacao.percentualGordura
        )
}
