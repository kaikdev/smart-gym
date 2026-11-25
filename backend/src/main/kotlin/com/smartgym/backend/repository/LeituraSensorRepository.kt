package com.smartgym.backend.repository

import com.smartgym.backend.entity.LeituraSensor
import org.springframework.data.jpa.repository.JpaRepository

interface LeituraSensorRepository : JpaRepository<LeituraSensor, Long> {

    fun findTop100ByOrderByDataHoraLeituraDesc(): List<LeituraSensor>

    fun findTop100ByCodigoSensorOrderByDataHoraLeituraDesc(codigoSensor: String): List<LeituraSensor>
}
