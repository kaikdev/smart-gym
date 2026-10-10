package com.smartgym.backend.controller

import com.smartgym.backend.dto.AvaliacaoFisicaRequest
import com.smartgym.backend.dto.AvaliacaoFisicaResponse
import com.smartgym.backend.service.AvaliacaoFisicaService
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
@RequestMapping("/api/avaliacoes")
class AvaliacaoFisicaController(private val avaliacaoFisicaService: AvaliacaoFisicaService) {

    // GET /api/avaliacoes -> lista todas
    @GetMapping
    fun listar(): List<AvaliacaoFisicaResponse> = avaliacaoFisicaService.listar()

    // GET /api/avaliacoes/{id} -> busca uma pelo id
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): AvaliacaoFisicaResponse = avaliacaoFisicaService.buscarPorId(id)

    // GET /api/avaliacoes/buscar?termo=carl -> busca parcial pelo avaliador (JPQL LIKE)
    @GetMapping("/buscar")
    fun buscarPorAvaliador(@RequestParam termo: String): List<AvaliacaoFisicaResponse> =
        avaliacaoFisicaService.buscarPorAvaliador(termo)

    // POST /api/avaliacoes -> cria
    @PostMapping
    fun criar(@Valid @RequestBody request: AvaliacaoFisicaRequest): ResponseEntity<AvaliacaoFisicaResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(avaliacaoFisicaService.criar(request))

    // PUT /api/avaliacoes/{id} -> atualiza
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: AvaliacaoFisicaRequest): AvaliacaoFisicaResponse =
        avaliacaoFisicaService.atualizar(id, request)

    // DELETE /api/avaliacoes/{id} -> remove
    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): ResponseEntity<Void> {
        avaliacaoFisicaService.remover(id)
        return ResponseEntity.noContent().build()
    }
}
