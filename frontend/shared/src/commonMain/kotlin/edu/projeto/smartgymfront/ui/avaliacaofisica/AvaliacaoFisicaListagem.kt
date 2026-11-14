package edu.projeto.smartgymfront.ui.avaliacaofisica

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.projeto.smartgymfront.ui.componentes.CardRegistro
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.viewmodel.AvaliacaoFisicaViewModel

@Composable
fun AvaliacaoFisicaListagem(
    viewModel: AvaliacaoFisicaViewModel,
    aoNovo: () -> Unit,
    aoEditar: (Long) -> Unit,
    aoVoltar: () -> Unit
) {
    Tela("Avaliações Físicas", aoVoltar) {
        Button(onClick = aoNovo, modifier = Modifier.fillMaxWidth()) {
            Text("Nova avaliação")
        }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        if (viewModel.lista.isEmpty()) Text("Nenhum registro encontrado.")
        viewModel.lista.forEach { item ->
            val id = item.id ?: return@forEach
            CardRegistro(
                titulo = "#${item.id} - ${item.avaliador}",
                campos = listOf(
                    "Data: ${dataParaTexto(item.dataAvaliacao)}",
                    "Altura: ${item.altura} m",
                    "IMC: ${item.imc}",
                    "Gordura: ${item.percentualGordura?.let { "$it%" } ?: "-"}"
                ),
                aoEditar = { aoEditar(id) },
                aoApagar = { viewModel.apagar(id) }
            )
        }
    }
}
