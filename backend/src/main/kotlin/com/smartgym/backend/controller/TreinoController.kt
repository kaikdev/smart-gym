package com.smartgym.backend.controller

import com.smartgym.backend.dto.TreinoRequest
import com.smartgym.backend.dto.TreinoResponse
import com.smartgym.backend.service.TreinoService
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
@RequestMapping("/api/treinos")
class TreinoController(private val treinoService: TreinoService) {

    // GET /api/treinos -> lista todos
    @GetMapping
    fun listar(): List<TreinoResponse> = treinoService.listar()

    // GET /api/treinos/{id} -> busca um pelo id
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): TreinoResponse = treinoService.buscarPorId(id)

    // GET /api/treinos/buscar?termo=hiper -> busca parcial pelo título (JPQL LIKE)
    @GetMapping("/buscar")
    fun buscarPorTitulo(@RequestParam termo: String): List<TreinoResponse> = treinoService.buscarPorTitulo(termo)

    // POST /api/treinos -> cria
    @PostMapping
    fun criar(@Valid @RequestBody request: TreinoRequest): ResponseEntity<TreinoResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(treinoService.criar(request))

    // PUT /api/treinos/{id} -> atualiza
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: TreinoRequest): TreinoResponse =
        treinoService.atualizar(id, request)

    // DELETE /api/treinos/{id} -> remove
    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): ResponseEntity<Void> {
        treinoService.remover(id)
        return ResponseEntity.noContent().build()
    }
}
