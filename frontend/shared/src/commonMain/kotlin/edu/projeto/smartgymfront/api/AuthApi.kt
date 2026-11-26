package edu.projeto.smartgymfront.api

import edu.projeto.smartgymfront.sessao.SessaoUsuario
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val senha: String)

@Serializable
data class RegisterRequest(val nome: String, val email: String, val senha: String)

@Serializable
data class TokenResponse(val token: String, val perfil: String = "ALUNO", val tipo: String = "Bearer")

object AuthApi {

    suspend fun entrar(email: String, senha: String) {
        val resposta: TokenResponse = ApiClient.criar("api/auth/login", LoginRequest(email, senha))
        SessaoUsuario.iniciar(resposta.token, email, resposta.perfil)
    }

    suspend fun registrar(nome: String, email: String, senha: String) {
        val resposta: TokenResponse = ApiClient.criar("api/auth/register", RegisterRequest(nome, email, senha))
        SessaoUsuario.iniciar(resposta.token, email, resposta.perfil)
    }

    fun sair() {
        SessaoUsuario.encerrar()
    }
}
