package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

/**
 * Ficha de treino montada para os alunos.
 */
@Entity
@Table(name = "treinos")
class Treino(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "titulo", length = 80, nullable = false)
    var titulo: String = "",

    @Column(name = "objetivo", length = 100)
    var objetivo: String? = null,

    // Preenchida automaticamente pelo serviço com a data do cadastro
    @Column(name = "data_criacao", nullable = false)
    var dataCriacao: LocalDate = LocalDate.now(),

    @Column(name = "duracao_estimada_min", nullable = false)
    var duracaoEstimadaMin: Int = 1,

    @Column(name = "publico", nullable = false)
    var publico: Boolean = false
)
