package com.smartgym.backend.mapper

import com.smartgym.backend.dto.TreinoRequest
import com.smartgym.backend.dto.TreinoResponse
import com.smartgym.backend.entity.Treino
import org.springframework.stereotype.Component
import java.time.LocalDate

/**
 * Converte Treino <-> DTOs.
 */
@Component
class TreinoMapper {

    // No cadastro, a data de criação é sempre a data de hoje
    fun toEntity(request: TreinoRequest): Treino =
        Treino(
            titulo = request.titulo!!,
            objetivo = request.objetivo,
            dataCriacao = LocalDate.now(),
            duracaoEstimadaMin = request.duracaoEstimadaMin!!,
            publico = request.publico ?: false
        )

    // Na atualização, a data de criação original é mantida
    fun updateEntity(treino: Treino, request: TreinoRequest) {
        treino.titulo = request.titulo!!
        treino.objetivo = request.objetivo
        treino.duracaoEstimadaMin = request.duracaoEstimadaMin!!
        treino.publico = request.publico ?: false
    }

    fun toResponse(treino: Treino): TreinoResponse =
        TreinoResponse(
            id = treino.id,
            titulo = treino.titulo,
            objetivo = treino.objetivo,
            dataCriacao = treino.dataCriacao,
            duracaoEstimadaMin = treino.duracaoEstimadaMin,
            publico = treino.publico
        )
}
