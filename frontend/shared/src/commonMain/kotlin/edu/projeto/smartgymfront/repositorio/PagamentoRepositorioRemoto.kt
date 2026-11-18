package edu.projeto.smartgymfront.repositorio

import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.model.Pagamento
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class PagamentoRepositorioRemoto {

    private val caminho = "api/pagamentos"

    suspend fun listar(): List<Pagamento> =
        ApiClient.executar { ApiClient.http.get(caminho) }

    suspend fun buscarPorId(id: Long): Pagamento =
        ApiClient.executar { ApiClient.http.get("$caminho/$id") }

    suspend fun criar(pagamento: Pagamento): Pagamento =
        ApiClient.executar { ApiClient.http.post(caminho) { setBody(pagamento) } }

    suspend fun atualizar(id: Long, pagamento: Pagamento): Pagamento =
        ApiClient.executar { ApiClient.http.put("$caminho/$id") { setBody(pagamento) } }

    suspend fun remover(id: Long) {
        ApiClient.enviar { ApiClient.http.delete("$caminho/$id") }
    }
}
