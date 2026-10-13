package edu.projeto.smartgymfront.ui.componentes

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import kotlin.time.Clock

fun hoje(): LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

fun textoParaData(texto: String): LocalDate? {
    val partes = texto.trim().split("/")
    if (partes.size != 3) return null
    val dia = partes[0].toIntOrNull() ?: return null
    val mes = partes[1].toIntOrNull() ?: return null
    val ano = partes[2].toIntOrNull() ?: return null
    if (ano < 1000) return null
    return try {
        LocalDate(ano, mes, dia)
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun dataParaTexto(data: LocalDate?): String {
    if (data == null) return ""
    val dia = data.day.toString().padStart(2, '0')
    val mes = data.month.ordinal.plus(1).toString().padStart(2, '0')
    return "$dia/$mes/${data.year}"
}

fun textoParaDouble(texto: String): Double? = texto.trim().replace(",", ".").toDoubleOrNull()

fun textoParaFloat(texto: String): Float? = texto.trim().replace(",", ".").toFloatOrNull()

fun textoParaInt(texto: String): Int? = texto.trim().toIntOrNull()
