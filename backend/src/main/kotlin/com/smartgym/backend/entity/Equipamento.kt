package com.smartgym.backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

/**
 * Equipamento (máquina) da academia, com o sensor de presença IoT acoplado.
 */
@Entity
@Table(name = "equipamentos")
class Equipamento(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "nome", length = 100, nullable = false)
    var nome: String = "",

    @Column(name = "fabricante", length = 50)
    var fabricante: String? = null,

    @Column(name = "data_compra")
    var dataCompra: LocalDate? = null,

    @Column(name = "peso_suportado", nullable = false)
    var pesoSuportado: Double = 1.0,

    // Cada máquina tem o seu próprio sensor, por isso o código é único
    @Column(name = "codigo_sensor_iot", length = 50, unique = true)
    var codigoSensorIot: String? = null,

    // Atualizado pelo sensor de presença (true = máquina ocupada)
    @Column(name = "em_uso_iot", nullable = false)
    var emUsoIot: Boolean = false
)
