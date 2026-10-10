package com.smartgym.backend.controller

import com.smartgym.backend.dto.UsuarioRequest
import com.smartgym.backend.dto.UsuarioResponse
import com.smartgym.backend.service.UsuarioService
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
@RequestMapping("/api/usuarios")
class UsuarioController(private val usuarioService: UsuarioService) {

    // GET /api/usuarios -> lista todos
    @GetMapping
    fun listar(): List<UsuarioResponse> = usuarioService.listar()

    // GET /api/usuarios/{id} -> busca um pelo id
    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): UsuarioResponse = usuarioService.buscarPorId(id)

    // GET /api/usuarios/buscar?termo=jo -> busca parcial pelo nome (JPQL LIKE)
    @GetMapping("/buscar")
    fun buscarPorNome(@RequestParam termo: String): List<UsuarioResponse> = usuarioService.buscarPorNome(termo)

    // POST /api/usuarios -> cria
    @PostMapping
    fun criar(@Valid @RequestBody request: UsuarioRequest): ResponseEntity<UsuarioResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criar(request))

    // PUT /api/usuarios/{id} -> atualiza
    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @Valid @RequestBody request: UsuarioRequest): UsuarioResponse =
        usuarioService.atualizar(id, request)

    // DELETE /api/usuarios/{id} -> remove
    @DeleteMapping("/{id}")
    fun remover(@PathVariable id: Long): ResponseEntity<Void> {
        usuarioService.remover(id)
        return ResponseEntity.noContent().build()
    }
}
