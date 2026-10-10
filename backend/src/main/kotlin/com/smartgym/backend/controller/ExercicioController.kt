package com.smartgym.backend.controller

import com.smartgym.backend.dto.ExercicioRequest
import com.smartgym.backend.dto.ExercicioResponse
import com.smartgym.backend.service.ExercicioService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/exercicios")
class ExercicioController(private val exercicioService: ExercicioService) {

    // GET /api/exercicios -> lista todos
    @GetMapping
    fun listar(): List<ExercicioResponse> = exercicioService.listar()

    // GET /api/exercicios/{id} -> busca um pelo id
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ExercicioResponse = exercicioService.buscarPorId(id)

    // GET /api/exercicios/buscar?termo=sup -> busca parcial pelo nome (JPQL LIKE)
    @GetMapping("/buscar")
    fun buscarPorNome(@RequestParam termo: String): List<ExercicioResponse> = exercicioService.buscarPorNome(termo)

    // POST /api/exercicios -> cria
    @PostMapping
    fun criar(@Valid @RequestBody request: ExercicioRequest): ResponseEntity<ExercicioResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(exercicioService.criar(request))

    // PUT /api/exercicios/{id} -> atualiza
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: ExercicioRequest): ExercicioResponse =
        exercicioService.atualizar(id, request)

    // DELETE /api/exercicios/{id} -> remove
    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): ResponseEntity<Void> {
        exercicioService.remover(id)
        return ResponseEntity.noContent().build()
    }
}
