package com.smartgym.backend.mapper

import com.smartgym.backend.dto.ExercicioRequest
import com.smartgym.backend.dto.ExercicioResponse
import com.smartgym.backend.entity.Exercicio
import org.springframework.stereotype.Component

/**
 * Converte Exercicio <-> DTOs.
 */
@Component
class ExercicioMapper {

    fun toEntity(request: ExercicioRequest): Exercicio =
        Exercicio(
            nome = request.nome!!,
            grupoMuscular = request.grupoMuscular!!,
            series = request.series!!,
            repeticoes = request.repeticoes!!,
            cargaSugerida = request.cargaSugerida,
            precisaAcompanhamento = request.precisaAcompanhamento ?: false
        )

    fun updateEntity(exercicio: Exercicio, request: ExercicioRequest) {
        exercicio.nome = request.nome!!
        exercicio.grupoMuscular = request.grupoMuscular!!
        exercicio.series = request.series!!
        exercicio.repeticoes = request.repeticoes!!
        exercicio.cargaSugerida = request.cargaSugerida
        exercicio.precisaAcompanhamento = request.precisaAcompanhamento ?: false
    }

    fun toResponse(exercicio: Exercicio): ExercicioResponse =
        ExercicioResponse(
            id = exercicio.id,
            nome = exercicio.nome,
            grupoMuscular = exercicio.grupoMuscular,
            series = exercicio.series,
            repeticoes = exercicio.repeticoes,
            cargaSugerida = exercicio.cargaSugerida,
            precisaAcompanhamento = exercicio.precisaAcompanhamento
        )
}
