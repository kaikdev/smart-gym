package edu.projeto.smartgymfront.api

import edu.projeto.smartgymfront.urlBackend
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ErroResponse(
    val status: Int = 0,
    val mensagem: String = "",
    val erros: Map<String, String> = emptyMap()
)

class ApiException(
    val mensagem: String,
    val erros: Map<String, String> = emptyMap()
) : Exception(mensagem)

object ApiClient {

    var token: String? = null
    var emailLogado: String? = null

    val http = HttpClient {
        expectSuccess = false
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        defaultRequest {
            url("$urlBackend/")
            contentType(ContentType.Application.Json)
            token?.let { header(HttpHeaders.Authorization, "Bearer $it") }
        }
    }

    suspend inline fun <reified R> listar(caminho: String): List<R> =
        executar { http.get(caminho) }

    suspend inline fun <reified T, reified R> criar(caminho: String, corpo: T): R =
        executar { http.post(caminho) { setBody(corpo) } }

    suspend inline fun <reified R> executar(chamada: () -> HttpResponse): R {
        val resposta = try {
            chamada()
        } catch (e: Exception) {
            throw ApiException("Não foi possível conectar ao servidor ($urlBackend)")
        }
        if (resposta.status.isSuccess()) return resposta.body()
        val erro = runCatching { resposta.body<ErroResponse>() }.getOrNull()
        val mensagem = erro?.mensagem?.takeIf { it.isNotBlank() } ?: when (resposta.status.value) {
            401 -> "Sessão expirada. Faça login novamente."
            403 -> "Acesso negado."
            else -> "Erro ${resposta.status.value} ao comunicar com o servidor."
        }
        throw ApiException(mensagem, erro?.erros ?: emptyMap())
    }
}
