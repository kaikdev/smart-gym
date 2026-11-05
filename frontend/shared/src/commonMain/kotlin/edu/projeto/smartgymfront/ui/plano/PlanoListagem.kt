package edu.projeto.smartgymfront.ui.plano

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.projeto.smartgymfront.ui.componentes.CardRegistro
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.PlanoViewModel

@Composable
fun PlanoListagem(
    viewModel: PlanoViewModel,
    aoNovo: () -> Unit,
    aoEditar: (Long) -> Unit,
    aoVoltar: () -> Unit
) {
    Tela("Planos", aoVoltar) {
        Button(onClick = aoNovo, modifier = Modifier.fillMaxWidth()) {
            Text("Novo plano")
        }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        if (viewModel.lista.isEmpty()) Text("Nenhum registro encontrado.")
        viewModel.lista.forEach { item ->
            val id = item.id ?: return@forEach
            CardRegistro(
                titulo = "#${item.id} - ${item.descricao}",
                campos = listOf(
                    "Valor mensal: R$ ${item.valorMensal}",
                    "Fidelidade: ${item.mesesFidelidade} meses",
                    "Taxa de matrícula: R$ ${item.taxaMatricula}",
                    "Promocional: ${if (item.promocional) "Sim" else "Não"}"
                ),
                aoEditar = { aoEditar(id) },
                aoApagar = { viewModel.apagar(id) }
            )
        }
    }
}
