package edu.projeto.smartgymfront.repositorio

import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.model.Equipamento
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class EquipamentoRepositorioRemoto {

    private val caminho = "api/equipamentos"

    suspend fun listar(): List<Equipamento> =
        ApiClient.executar { ApiClient.http.get(caminho) }

    suspend fun buscarPorId(id: Long): Equipamento =
        ApiClient.executar { ApiClient.http.get("$caminho/$id") }

    suspend fun criar(equipamento: Equipamento): Equipamento =
        ApiClient.executar { ApiClient.http.post(caminho) { setBody(equipamento) } }

    suspend fun atualizar(id: Long, equipamento: Equipamento): Equipamento =
        ApiClient.executar { ApiClient.http.put("$caminho/$id") { setBody(equipamento) } }

    suspend fun remover(id: Long) {
        ApiClient.enviar { ApiClient.http.delete("$caminho/$id") }
    }
}
