package edu.projeto.smartgymfront.repositorio

import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.model.Treino
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class TreinoRepositorioRemoto {

    private val caminho = "api/treinos"

    suspend fun listar(): List<Treino> =
        ApiClient.executar { ApiClient.http.get(caminho) }

    suspend fun buscarPorId(id: Long): Treino =
        ApiClient.executar { ApiClient.http.get("$caminho/$id") }

    suspend fun criar(treino: Treino): Treino =
        ApiClient.executar { ApiClient.http.post(caminho) { setBody(treino) } }

    suspend fun atualizar(id: Long, treino: Treino): Treino =
        ApiClient.executar { ApiClient.http.put("$caminho/$id") { setBody(treino) } }

    suspend fun remover(id: Long) {
        ApiClient.enviar { ApiClient.http.delete("$caminho/$id") }
    }
}
