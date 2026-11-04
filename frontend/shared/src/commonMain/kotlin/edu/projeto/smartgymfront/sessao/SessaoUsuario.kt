package edu.projeto.smartgymfront.sessao

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SessaoUsuario {

    var token by mutableStateOf<String?>(null)
        private set

    var email by mutableStateOf<String?>(null)
        private set

    val autenticado: Boolean
        get() = token != null

    fun iniciar(token: String, email: String) {
        this.token = token
        this.email = email
    }

    fun encerrar() {
        token = null
        email = null
    }
}
