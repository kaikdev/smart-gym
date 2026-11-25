package com.smartgym.backend.dto

import java.time.LocalDateTime

data class LeituraSensorResponse(
    val id: Long?,
    val codigoSensor: String,
    val emUso: Boolean,
    val dataHoraLeitura: LocalDateTime,
    val equipamentoId: Long?
)
