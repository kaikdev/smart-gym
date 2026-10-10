package com.smartgym.backend.mapper

import com.smartgym.backend.dto.PlanoRequest
import com.smartgym.backend.dto.PlanoResponse
import com.smartgym.backend.entity.Plano
import org.springframework.stereotype.Component

/**
 * Converte Plano <-> DTOs.
 */
@Component
class PlanoMapper {

    fun toEntity(request: PlanoRequest): Plano =
        Plano(
            descricao = request.descricao!!,
            valorMensal = request.valorMensal!!,
            mesesFidelidade = request.mesesFidelidade ?: 0,
            taxaMatricula = request.taxaMatricula ?: 0.0,
            promocional = request.promocional ?: false
        )

    fun updateEntity(plano: Plano, request: PlanoRequest) {
        plano.descricao = request.descricao!!
        plano.valorMensal = request.valorMensal!!
        plano.mesesFidelidade = request.mesesFidelidade ?: 0
        plano.taxaMatricula = request.taxaMatricula ?: 0.0
        plano.promocional = request.promocional ?: false
    }

    fun toResponse(plano: Plano): PlanoResponse =
        PlanoResponse(
            id = plano.id,
            descricao = plano.descricao,
            valorMensal = plano.valorMensal,
            mesesFidelidade = plano.mesesFidelidade,
            taxaMatricula = plano.taxaMatricula,
            promocional = plano.promocional
        )
}
