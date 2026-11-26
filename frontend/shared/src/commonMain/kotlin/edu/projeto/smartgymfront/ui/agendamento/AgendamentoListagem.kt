package edu.projeto.smartgymfront.ui.agendamento

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.projeto.smartgymfront.sessao.SessaoUsuario
import edu.projeto.smartgymfront.ui.componentes.CardRegistro
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataHoraParaTexto
import edu.projeto.smartgymfront.viewmodel.AgendamentoViewModel

@Composable
fun AgendamentoListagem(
    viewModel: AgendamentoViewModel,
    aoNovo: () -> Unit,
    aoEditar: (Long) -> Unit,
    aoVoltar: () -> Unit
) {
    Tela("Agendamentos", aoVoltar) {
        if (SessaoUsuario.admin) {
            Button(onClick = aoNovo, modifier = Modifier.fillMaxWidth()) {
                Text("Novo agendamento")
            }
        }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        if (viewModel.lista.isEmpty()) Text("Nenhum registro encontrado.")
        viewModel.lista.forEach { item ->
            val id = item.id ?: return@forEach
            CardRegistro(
                titulo = "#${item.id} - ${item.tipoAula}",
                campos = listOf(
                    "Data e hora: ${dataHoraParaTexto(item.dataHoraAgendada)}",
                    "Instrutor: ${item.instrutor ?: "-"}",
                    "Capacidade máxima: ${item.capacidadeMaxima}",
                    "Situação: ${if (item.cancelado) "Cancelado" else "Ativo"}"
                ),
                aoEditar = { aoEditar(id) },
                aoApagar = { viewModel.apagar(id) }
            )
        }
    }
}
