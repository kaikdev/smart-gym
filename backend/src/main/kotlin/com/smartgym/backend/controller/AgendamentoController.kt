package com.smartgym.backend.controller

import com.smartgym.backend.dto.AgendamentoRequest
import com.smartgym.backend.dto.AgendamentoResponse
import com.smartgym.backend.service.AgendamentoService
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
@RequestMapping("/api/agendamentos")
class AgendamentoController(private val agendamentoService: AgendamentoService) {

    // GET /api/agendamentos -> lista todos
    @GetMapping
    fun listar(): List<AgendamentoResponse> = agendamentoService.listar()

    // GET /api/agendamentos/{id} -> busca um pelo id
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): AgendamentoResponse = agendamentoService.buscarPorId(id)

    // GET /api/agendamentos/buscar?termo=yoga -> busca parcial pelo tipo de aula (JPQL LIKE)
    @GetMapping("/buscar")
    fun buscarPorTipoAula(@RequestParam termo: String): List<AgendamentoResponse> =
        agendamentoService.buscarPorTipoAula(termo)

    // POST /api/agendamentos -> cria
    @PostMapping
    fun criar(@Valid @RequestBody request: AgendamentoRequest): ResponseEntity<AgendamentoResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(request))

    // PUT /api/agendamentos/{id} -> atualiza
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: AgendamentoRequest): AgendamentoResponse =
        agendamentoService.atualizar(id, request)

    // DELETE /api/agendamentos/{id} -> remove
    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): ResponseEntity<Void> {
        agendamentoService.remover(id)
        return ResponseEntity.noContent().build()
    }
}
