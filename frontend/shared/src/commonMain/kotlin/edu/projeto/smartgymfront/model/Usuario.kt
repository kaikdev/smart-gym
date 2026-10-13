package edu.projeto.smartgymfront.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Long? = null,
    val nome: String = "",
    val email: String = "",
    val senha: String? = null,
    val dataNascimento: LocalDate? = null,
    val pesoAtual: Float? = null,
    val ativo: Boolean = true
)
