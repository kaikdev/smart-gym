package edu.projeto.smartgymfront.repositorio

import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.model.Plano
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class PlanoRepositorioRemoto {

    private val caminho = "api/planos"

    suspend fun listar(): List<Plano> =
        ApiClient.executar { ApiClient.http.get(caminho) }

    suspend fun buscarPorId(id: Long): Plano =
        ApiClient.executar { ApiClient.http.get("$caminho/$id") }

    suspend fun criar(plano: Plano): Plano =
        ApiClient.executar { ApiClient.http.post(caminho) { setBody(plano) } }

    suspend fun atualizar(id: Long, plano: Plano): Plano =
        ApiClient.executar { ApiClient.http.put("$caminho/$id") { setBody(plano) } }

    suspend fun remover(id: Long) {
        ApiClient.enviar { ApiClient.http.delete("$caminho/$id") }
    }
}
