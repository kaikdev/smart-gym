package edu.projeto.smartgymfront.ui.treino

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.projeto.smartgymfront.sessao.SessaoUsuario
import edu.projeto.smartgymfront.ui.componentes.CardRegistro
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.viewmodel.TreinoViewModel

@Composable
fun TreinoListagem(
    viewModel: TreinoViewModel,
    aoNovo: () -> Unit,
    aoEditar: (Long) -> Unit,
    aoVoltar: () -> Unit
) {
    Tela("Treinos", aoVoltar) {
        if (SessaoUsuario.admin) {
            Button(onClick = aoNovo, modifier = Modifier.fillMaxWidth()) {
                Text("Novo treino")
            }
        }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        if (viewModel.lista.isEmpty()) Text("Nenhum registro encontrado.")
        viewModel.lista.forEach { item ->
            val id = item.id ?: return@forEach
            CardRegistro(
                titulo = "#${item.id} - ${item.titulo}",
                campos = listOf(
                    "Objetivo: ${item.objetivo ?: "-"}",
                    "Duração estimada: ${item.duracaoEstimadaMin} min",
                    "Criado em: ${dataParaTexto(item.dataCriacao).ifBlank { "-" }}",
                    "Visibilidade: ${if (item.publico) "Público" else "Privado"}"
                ),
                aoEditar = { aoEditar(id) },
                aoApagar = { viewModel.apagar(id) }
            )
        }
    }
}
