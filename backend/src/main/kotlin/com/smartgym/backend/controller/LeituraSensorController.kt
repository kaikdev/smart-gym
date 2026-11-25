package com.smartgym.backend.controller

import com.smartgym.backend.dto.LeituraSensorResponse
import com.smartgym.backend.service.LeituraSensorService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/leituras-sensor")
class LeituraSensorController(private val leituraSensorService: LeituraSensorService) {

    @GetMapping
    fun listar(): List<LeituraSensorResponse> = leituraSensorService.listar()

    @GetMapping("/sensor/{codigoSensor}")
    fun listarPorSensor(@PathVariable codigoSensor: String): List<LeituraSensorResponse> =
        leituraSensorService.listarPorSensor(codigoSensor)
}
