package com.smartgym.backend.controller

import com.smartgym.backend.dto.EquipamentoRequest
import com.smartgym.backend.dto.EquipamentoResponse
import com.smartgym.backend.service.EquipamentoService
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
@RequestMapping("/api/equipamentos")
class EquipamentoController(private val equipamentoService: EquipamentoService) {

    // GET /api/equipamentos -> lista todos
    @GetMapping
    fun listar(): List<EquipamentoResponse> = equipamentoService.listar()

    // GET /api/equipamentos/{id} -> busca um pelo id
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): EquipamentoResponse = equipamentoService.buscarPorId(id)

    // GET /api/equipamentos/buscar?termo=leg -> busca parcial pelo nome (JPQL LIKE)
    @GetMapping("/buscar")
    fun buscarPorNome(@RequestParam termo: String): List<EquipamentoResponse> = equipamentoService.buscarPorNome(termo)

    // POST /api/equipamentos -> cria
    @PostMapping
    fun criar(@Valid @RequestBody request: EquipamentoRequest): ResponseEntity<EquipamentoResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(equipamentoService.criar(request))

    // PUT /api/equipamentos/{id} -> atualiza
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: EquipamentoRequest): EquipamentoResponse =
        equipamentoService.atualizar(id, request)

    // DELETE /api/equipamentos/{id} -> remove
    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): ResponseEntity<Void> {
        equipamentoService.remover(id)
        return ResponseEntity.noContent().build()
    }
}
