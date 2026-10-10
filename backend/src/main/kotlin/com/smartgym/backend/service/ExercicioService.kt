package com.smartgym.backend.service

import com.smartgym.backend.dto.ExercicioRequest
import com.smartgym.backend.dto.ExercicioResponse
import com.smartgym.backend.exception.NotFoundException
import com.smartgym.backend.mapper.ExercicioMapper
import com.smartgym.backend.repository.ExercicioRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Regras de negócio (CRUD) do Exercicio.
 */
@Service
class ExercicioService(
    private val exercicioRepository: ExercicioRepository,
    private val exercicioMapper: ExercicioMapper
) {

    @Transactional
    fun criar(request: ExercicioRequest): ExercicioResponse {
        val exercicio = exercicioMapper.toEntity(request)
        return exercicioMapper.toResponse(exercicioRepository.save(exercicio))
    }

    @Transactional(readOnly = true)
    fun listar(): List<ExercicioResponse> =
        exercicioRepository.findAll().map { exercicioMapper.toResponse(it) }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): ExercicioResponse =
        exercicioMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun buscarPorNome(termo: String): List<ExercicioResponse> =
        exercicioRepository.buscarPorNomeParcial(termo).map { exercicioMapper.toResponse(it) }

    @Transactional
    fun atualizar(id: Long, request: ExercicioRequest): ExercicioResponse {
        val exercicio = buscarEntidade(id)
        exercicioMapper.updateEntity(exercicio, request)
        return exercicioMapper.toResponse(exercicioRepository.save(exercicio))
    }

    @Transactional
    fun remover(id: Long) {
        exercicioRepository.delete(buscarEntidade(id))
    }

    private fun buscarEntidade(id: Long) =
        exercicioRepository.findById(id).orElseThrow { NotFoundException("Exercício $id não encontrado") }
}
