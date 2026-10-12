package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

/**
 * Agendamento de aula coletiva na academia.
 */
@Entity
@Table(name = "agendamentos")
class Agendamento(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "tipo_aula", length = 60, nullable = false)
    var tipoAula: String = "",

    @Column(name = "instrutor", length = 100)
    var instrutor: String? = null,

    @Column(name = "data_hora_agendada", nullable = false)
    var dataHoraAgendada: LocalDateTime = LocalDateTime.now(),

    @Column(name = "capacidade_maxima", nullable = false)
    var capacidadeMaxima: Int = 1,

    @Column(name = "cancelado", nullable = false)
    var cancelado: Boolean = false
)
