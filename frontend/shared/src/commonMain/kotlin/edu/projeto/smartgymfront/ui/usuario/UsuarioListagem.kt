package edu.projeto.smartgymfront.ui.usuario

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.projeto.smartgymfront.ui.componentes.CardRegistro
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.viewmodel.UsuarioViewModel

@Composable
fun UsuarioListagem(
    viewModel: UsuarioViewModel,
    aoNovo: () -> Unit,
    aoEditar: (Long) -> Unit,
    aoVoltar: () -> Unit
) {
    Tela("Usuários", aoVoltar) {
        Button(onClick = aoNovo, modifier = Modifier.fillMaxWidth()) {
            Text("Novo usuário")
        }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        if (viewModel.lista.isEmpty()) Text("Nenhum registro encontrado.")
        viewModel.lista.forEach { item ->
            val id = item.id ?: return@forEach
            CardRegistro(
                titulo = "#${item.id} - ${item.nome}",
                campos = listOf(
                    "E-mail: ${item.email}",
                    "Nascimento: ${dataParaTexto(item.dataNascimento).ifBlank { "-" }}",
                    "Peso: ${item.pesoAtual?.let { "$it kg" } ?: "-"}",
                    "Situação: ${if (item.ativo) "Ativo" else "Inativo"}"
                ),
                aoEditar = { aoEditar(id) },
                aoApagar = { viewModel.apagar(id) }
            )
        }
    }
}
