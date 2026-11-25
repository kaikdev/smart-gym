package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "leituras_sensor")
class LeituraSensor(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "codigo_sensor", length = 50, nullable = false)
    var codigoSensor: String = "",

    @Column(name = "em_uso", nullable = false)
    var emUso: Boolean = false,

    @Column(name = "data_hora_leitura", nullable = false)
    var dataHoraLeitura: LocalDateTime = LocalDateTime.now(),

    @Column(name = "equipamento_id")
    var equipamentoId: Long? = null
)
