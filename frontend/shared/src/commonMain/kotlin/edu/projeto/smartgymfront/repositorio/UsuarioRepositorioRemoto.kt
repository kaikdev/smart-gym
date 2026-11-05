package edu.projeto.smartgymfront.repositorio

import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.model.Usuario
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class UsuarioRepositorioRemoto {

    private val caminho = "api/usuarios"

    suspend fun listar(): List<Usuario> =
        ApiClient.executar { ApiClient.http.get(caminho) }

    suspend fun buscarPorId(id: Long): Usuario =
        ApiClient.executar { ApiClient.http.get("$caminho/$id") }

    suspend fun criar(usuario: Usuario): Usuario =
        ApiClient.executar { ApiClient.http.post(caminho) { setBody(usuario) } }

    suspend fun atualizar(id: Long, usuario: Usuario): Usuario =
        ApiClient.executar { ApiClient.http.put("$caminho/$id") { setBody(usuario) } }

    suspend fun remover(id: Long) {
        ApiClient.enviar { ApiClient.http.delete("$caminho/$id") }
    }
}
