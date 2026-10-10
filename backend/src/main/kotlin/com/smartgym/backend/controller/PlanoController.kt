package com.smartgym.backend.controller

import com.smartgym.backend.dto.PlanoRequest
import com.smartgym.backend.dto.PlanoResponse
import com.smartgym.backend.service.PlanoService
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
@RequestMapping("/api/planos")
class PlanoController(private val planoService: PlanoService) {

    // GET /api/planos -> lista todos
    @GetMapping
    fun listar(): List<PlanoResponse> = planoService.listar()

    // GET /api/planos/{id} -> busca um pelo id
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): PlanoResponse = planoService.buscarPorId(id)

    // GET /api/planos/buscar?termo=mens -> busca parcial pela descrição (JPQL LIKE)
    @GetMapping("/buscar")
    fun buscarPorDescricao(@RequestParam termo: String): List<PlanoResponse> = planoService.buscarPorDescricao(termo)

    // POST /api/planos -> cria
    @PostMapping
    fun criar(@Valid @RequestBody request: PlanoRequest): ResponseEntity<PlanoResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(planoService.criar(request))

    // PUT /api/planos/{id} -> atualiza
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: PlanoRequest): PlanoResponse =
        planoService.atualizar(id, request)

    // DELETE /api/planos/{id} -> remove
    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): ResponseEntity<Void> {
        planoService.remover(id)
        return ResponseEntity.noContent().build()
    }
}