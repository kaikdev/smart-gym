package com.smartgym.backend.exception

/**
 * Lançada quando o dado já existe, como um e-mail repetido (vira HTTP 409).
 */
class ConflictException(mensagem: String) : RuntimeException(mensagem)
