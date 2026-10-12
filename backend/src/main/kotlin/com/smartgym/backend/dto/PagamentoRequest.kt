package com.smartgym.backend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

/**
 * Dados recebidos na requisição (POST/PUT de pagamento).
 * A data de processamento não é enviada: o sistema preenche com a data e hora do cadastro.
 */
data class PagamentoRequest(

    @field:NotBlank(message = "O método de pagamento é obrigatório")
    @field:Size(max = 40, message = "O método deve ter no máximo 40 caracteres")
    val metodo: String? = null,

    @field:Size(max = 100, message = "O código da transação deve ter no máximo 100 caracteres")
    val codigoTransacao: String? = null,

    @field:NotNull(message = "O valor final é obrigatório")
    @field:Positive(message = "O valor final deve ser maior que zero")
    val valorFinal: Double? = null,

    val confirmado: Boolean? = false
)
