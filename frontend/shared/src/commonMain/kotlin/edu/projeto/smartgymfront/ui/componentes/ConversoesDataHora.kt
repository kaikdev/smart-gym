package edu.projeto.smartgymfront.ui.componentes

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

fun agora(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun textoParaDataHora(texto: String): LocalDateTime? {
    val partes = texto.trim().split(Regex("\\s+"))
    if (partes.size != 2) return null
    val data = textoParaData(partes[0]) ?: return null
    val horaMinuto = partes[1].split(":")
    if (horaMinuto.size != 2) return null
    val hora = horaMinuto[0].toIntOrNull() ?: return null
    val minuto = horaMinuto[1].toIntOrNull() ?: return null
    return try {
        LocalDateTime(data, LocalTime(hora, minuto))
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun dataHoraParaTexto(dataHora: LocalDateTime?): String {
    if (dataHora == null) return ""
    val hora = dataHora.hour.toString().padStart(2, '0')
    val minuto = dataHora.minute.toString().padStart(2, '0')
    return "${dataParaTexto(dataHora.date)} $hora:$minuto"
}
