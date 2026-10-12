package com.smartgym.backend.controller

import com.smartgym.backend.dto.PagamentoRequest
import com.smartgym.backend.dto.PagamentoResponse
import com.smartgym.backend.service.PagamentoService
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
@RequestMapping("/api/pagamentos")
class PagamentoController(private val pagamentoService: PagamentoService) {

    // GET /api/pagamentos -> lista todos
    @GetMapping
    fun listar(): List<PagamentoResponse> = pagamentoService.listar()

    // GET /api/pagamentos/{id} -> busca um pelo id
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): PagamentoResponse = pagamentoService.buscarPorId(id)

    // GET /api/pagamentos/buscar?termo=pix -> busca parcial pelo método (JPQL LIKE)
    @GetMapping("/buscar")
    fun buscarPorMetodo(@RequestParam termo: String): List<PagamentoResponse> = pagamentoService.buscarPorMetodo(termo)

    // POST /api/pagamentos -> cria
    @PostMapping
    fun criar(@Valid @RequestBody request: PagamentoRequest): ResponseEntity<PagamentoResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.criar(request))

    // PUT /api/pagamentos/{id} -> atualiza
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: PagamentoRequest): PagamentoResponse =
        pagamentoService.atualizar(id, request)

    // DELETE /api/pagamentos/{id} -> remove
    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): ResponseEntity<Void> {
        pagamentoService.remover(id)
        return ResponseEntity.noContent().build()
    }
}
