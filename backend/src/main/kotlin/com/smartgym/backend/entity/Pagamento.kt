package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * Pagamento registrado no caixa da academia.
 */
@Entity
@Table(name = "pagamentos")
class Pagamento(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // Ex.: PIX, Cartão de crédito, Boleto, Dinheiro
    @Column(name = "metodo", length = 40, nullable = false)
    var metodo: String = "",

    // Código devolvido pela operadora; não pode se repetir
    @Column(name = "codigo_transacao", length = 100, unique = true)
    var codigoTransacao: String? = null,

    // Preenchida automaticamente pelo serviço com a data e hora do cadastro
    @Column(name = "data_processamento", nullable = false)
    var dataProcessamento: LocalDateTime = LocalDateTime.now(),

    @Column(name = "valor_final", nullable = false)
    var valorFinal: Double = 0.0,

    @Column(name = "confirmado", nullable = false)
    var confirmado: Boolean = false
)
