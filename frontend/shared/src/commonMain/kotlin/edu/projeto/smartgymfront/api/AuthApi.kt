package edu.projeto.smartgymfront.api

import kotlinx.serialization.Serializable

@Serializable
data class SigninRequest(val email: String, val senha: String)

@Serializable
data class TokenResponse(val token: String, val tipo: String = "Bearer")

@Serializable
data class SignupRequest(val nome: String, val email: String, val senha: String)

@Serializable
data class SignupResponse(val id: Long? = null, val nome: String = "", val email: String = "")

object AuthApi {

    suspend fun entrar(email: String, senha: String) {
        val resposta: TokenResponse = ApiClient.criar("api/auth/signin", SigninRequest(email, senha))
        ApiClient.token = resposta.token
        ApiClient.emailLogado = email
    }

    suspend fun cadastrar(nome: String, email: String, senha: String): SignupResponse =
        ApiClient.criar("api/auth/signup", SignupRequest(nome, email, senha))

    fun sair() {
        ApiClient.token = null
        ApiClient.emailLogado = null
    }
}
