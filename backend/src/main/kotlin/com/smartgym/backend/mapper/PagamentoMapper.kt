package com.smartgym.backend.mapper

import com.smartgym.backend.dto.PagamentoRequest
import com.smartgym.backend.dto.PagamentoResponse
import com.smartgym.backend.entity.Pagamento
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Converte Pagamento <-> DTOs.
 */
@Component
class PagamentoMapper {

    // No cadastro, a data de processamento é sempre a data e hora atual
    fun toEntity(request: PagamentoRequest): Pagamento =
        Pagamento(
            metodo = request.metodo!!,
            codigoTransacao = request.codigoTransacao?.ifBlank { null },
            dataProcessamento = LocalDateTime.now(),
            valorFinal = request.valorFinal!!,
            confirmado = request.confirmado ?: false
        )

    // Na atualização, a data de processamento original é mantida
    fun updateEntity(pagamento: Pagamento, request: PagamentoRequest) {
        pagamento.metodo = request.metodo!!
        pagamento.codigoTransacao = request.codigoTransacao?.ifBlank { null }
        pagamento.valorFinal = request.valorFinal!!
        pagamento.confirmado = request.confirmado ?: false
    }

    fun toResponse(pagamento: Pagamento): PagamentoResponse =
        PagamentoResponse(
            id = pagamento.id,
            metodo = pagamento.metodo,
            codigoTransacao = pagamento.codigoTransacao,
            dataProcessamento = pagamento.dataProcessamento,
            valorFinal = pagamento.valorFinal,
            confirmado = pagamento.confirmado
        )
}
