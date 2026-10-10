package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

/**
 * Avaliação física realizada por um avaliador da academia.
 */
@Entity
@Table(name = "avaliacoes_fisicas")
class AvaliacaoFisica(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "avaliador", length = 100, nullable = false)
    var avaliador: String = "",

    @Column(name = "data_avaliacao", nullable = false)
    var dataAvaliacao: LocalDate = LocalDate.now(),

    // Altura em metros (ex.: 1.75)
    @Column(name = "altura", nullable = false)
    var altura: Float = 0f,

    @Column(name = "imc", nullable = false)
    var imc: Float = 0f,

    @Column(name = "percentual_gordura")
    var percentualGordura: Double? = null
)
