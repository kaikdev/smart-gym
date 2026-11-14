package edu.projeto.smartgymfront.repositorio

import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.model.AvaliacaoFisica
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class AvaliacaoFisicaRepositorioRemoto {

    private val caminho = "api/avaliacoes"

    suspend fun listar(): List<AvaliacaoFisica> =
        ApiClient.executar { ApiClient.http.get(caminho) }

    suspend fun buscarPorId(id: Long): AvaliacaoFisica =
        ApiClient.executar { ApiClient.http.get("$caminho/$id") }

    suspend fun criar(avaliacao: AvaliacaoFisica): AvaliacaoFisica =
        ApiClient.executar { ApiClient.http.post(caminho) { setBody(avaliacao) } }

    suspend fun atualizar(id: Long, avaliacao: AvaliacaoFisica): AvaliacaoFisica =
        ApiClient.executar { ApiClient.http.put("$caminho/$id") { setBody(avaliacao) } }

    suspend fun remover(id: Long) {
        ApiClient.enviar { ApiClient.http.delete("$caminho/$id") }
    }
}
