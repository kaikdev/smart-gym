package edu.projeto.smartgymfront.sessao

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object SessaoUsuario {

    var token by mutableStateOf<String?>(null)
        private set

    var email by mutableStateOf<String?>(null)
        private set

    var perfil by mutableStateOf<String?>(null)
        private set

    val autenticado: Boolean
        get() = token != null

    val admin: Boolean
        get() = perfil == "ADMIN"

    fun iniciar(token: String, email: String, perfil: String) {
        this.token = token
        this.email = email
        this.perfil = perfil
    }

    fun encerrar() {
        token = null
        email = null
        perfil = null
    }
}
