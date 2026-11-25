package com.smartgym.backend.mapper

import com.smartgym.backend.dto.LeituraSensorResponse
import com.smartgym.backend.entity.LeituraSensor
import org.springframework.stereotype.Component

@Component
class LeituraSensorMapper {

    fun toResponse(leitura: LeituraSensor): LeituraSensorResponse =
        LeituraSensorResponse(
            id = leitura.id,
            codigoSensor = leitura.codigoSensor,
            emUso = leitura.emUso,
            dataHoraLeitura = leitura.dataHoraLeitura,
            equipamentoId = leitura.equipamentoId
        )
}
