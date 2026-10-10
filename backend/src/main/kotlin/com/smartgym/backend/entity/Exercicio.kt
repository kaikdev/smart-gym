package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

/**
 * Exercício que pode compor as fichas de treino.
 */
@Entity
@Table(name = "exercicios")
class Exercicio(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nome", length = 100, nullable = false)
    var nome: String = "",

    @Column(name = "grupo_muscular", length = 50, nullable = false)
    var grupoMuscular: String = "",

    @Column(name = "series", nullable = false)
    var series: Int = 1,

    @Column(name = "repeticoes", nullable = false)
    var repeticoes: Int = 1,

    @Column(name = "carga_sugerida")
    var cargaSugerida: Float? = null,

    @Column(name = "precisa_acompanhamento", nullable = false)
    var precisaAcompanhamento: Boolean = false
)
