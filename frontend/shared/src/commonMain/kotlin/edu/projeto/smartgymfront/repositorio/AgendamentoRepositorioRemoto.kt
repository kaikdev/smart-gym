package edu.projeto.smartgymfront.repositorio

import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.model.Agendamento
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class AgendamentoRepositorioRemoto {

    private val caminho = "api/agendamentos"

    suspend fun listar(): List<Agendamento> =
        ApiClient.executar { ApiClient.http.get(caminho) }

    suspend fun buscarPorId(id: Long): Agendamento =
        ApiClient.executar { ApiClient.http.get("$caminho/$id") }

    suspend fun criar(agendamento: Agendamento): Agendamento =
        ApiClient.executar { ApiClient.http.post(caminho) { setBody(agendamento) } }

    suspend fun atualizar(id: Long, agendamento: Agendamento): Agendamento =
        ApiClient.executar { ApiClient.http.put("$caminho/$id") { setBody(agendamento) } }

    suspend fun remover(id: Long) {
        ApiClient.enviar { ApiClient.http.delete("$caminho/$id") }
    }
}
