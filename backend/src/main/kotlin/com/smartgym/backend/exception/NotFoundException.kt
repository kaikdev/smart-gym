package com.smartgym.backend.exception

/**
 * Lançada quando um registro não é encontrado (vira HTTP 404).
 */
class NotFoundException(mensagem: String) : RuntimeException(mensagem)
