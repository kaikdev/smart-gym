package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Plano de assinatura da academia.
 */
@Entity
@Table(name = "planos")
class Plano(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "descricao", length = 100, nullable = false)
    var descricao: String = "",

    @Column(name = "valor_mensal", nullable = false)
    var valorMensal: Double = 0.0,

    @Column(name = "meses_fidelidade", nullable = false)
    var mesesFidelidade: Int = 0,

    @Column(name = "taxa_matricula", nullable = false)
    var taxaMatricula: Double = 0.0,

    @Column(name = "promocional", nullable = false)
    var promocional: Boolean = false
)
