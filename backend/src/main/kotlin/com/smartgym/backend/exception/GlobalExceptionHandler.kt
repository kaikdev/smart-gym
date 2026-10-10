package com.smartgym.backend.exception

import com.smartgym.backend.dto.ErroResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Transforma as exceções em respostas HTTP organizadas (JSON).
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun tratarNaoEncontrado(ex: NotFoundException): ResponseEntity<ErroResponse> =
        resposta(HttpStatus.NOT_FOUND, ex.message ?: "Registro não encontrado")

    @ExceptionHandler(ConflictException::class)
    fun tratarConflito(ex: ConflictException): ResponseEntity<ErroResponse> =
        resposta(HttpStatus.CONFLICT, ex.message ?: "Registro já existe")

    // Erros de Bean Validation nos DTOs de Request
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun tratarValidacao(ex: MethodArgumentNotValidException): ResponseEntity<ErroResponse> {
        val erros = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Valor inválido") }
        return resposta(HttpStatus.BAD_REQUEST, "Dados inválidos", erros)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun tratarCredenciaisInvalidas(ex: BadCredentialsException): ResponseEntity<ErroResponse> =
        resposta(HttpStatus.UNAUTHORIZED, "E-mail ou senha inválidos")

    @ExceptionHandler(DisabledException::class)
    fun tratarUsuarioInativo(ex: DisabledException): ResponseEntity<ErroResponse> =
        resposta(HttpStatus.UNAUTHORIZED, "Usuário inativo")

    private fun resposta(
        status: HttpStatus,
        mensagem: String,
        erros: Map<String, String> = emptyMap()
    ): ResponseEntity<ErroResponse> =
        ResponseEntity.status(status).body(ErroResponse(status.value(), mensagem, erros))
}
