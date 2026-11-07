package edu.projeto.smartgymfront.repositorio

import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.model.Exercicio
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class ExercicioRepositorioRemoto {

    private val caminho = "api/exercicios"

    suspend fun listar(): List<Exercicio> =
        ApiClient.executar { ApiClient.http.get(caminho) }

    suspend fun buscarPorId(id: Long): Exercicio =
        ApiClient.executar { ApiClient.http.get("$caminho/$id") }

    suspend fun criar(exercicio: Exercicio): Exercicio =
        ApiClient.executar { ApiClient.http.post(caminho) { setBody(exercicio) } }

    suspend fun atualizar(id: Long, exercicio: Exercicio): Exercicio =
        ApiClient.executar { ApiClient.http.put("$caminho/$id") { setBody(exercicio) } }

    suspend fun remover(id: Long) {
        ApiClient.enviar { ApiClient.http.delete("$caminho/$id") }
    }
}
