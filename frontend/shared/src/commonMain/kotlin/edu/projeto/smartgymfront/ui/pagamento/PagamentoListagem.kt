package edu.projeto.smartgymfront.ui.pagamento

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
import edu.projeto.smartgymfront.viewmodel.PagamentoViewModel

@Composable
fun PagamentoListagem(
    viewModel: PagamentoViewModel,
    aoNovo: () -> Unit,
    aoEditar: (Long) -> Unit,
    aoVoltar: () -> Unit
) {
    Tela("Pagamentos", aoVoltar) {
        if (SessaoUsuario.admin) {
            Button(onClick = aoNovo, modifier = Modifier.fillMaxWidth()) {
                Text("Novo pagamento")
            }
        }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        if (viewModel.lista.isEmpty()) Text("Nenhum registro encontrado.")
        viewModel.lista.forEach { item ->
            val id = item.id ?: return@forEach
            CardRegistro(
                titulo = "#${item.id} - ${item.metodo}",
                campos = listOf(
                    "Valor final: R$ ${item.valorFinal}",
                    "Código da transação: ${item.codigoTransacao ?: "-"}",
                    "Processado em: ${dataHoraParaTexto(item.dataProcessamento).ifBlank { "-" }}",
                    "Situação: ${if (item.confirmado) "Confirmado" else "Pendente"}"
                ),
                aoEditar = { aoEditar(id) },
                aoApagar = { viewModel.apagar(id) }
            )
        }
    }
}
