package edu.projeto.smartgymfront.ui.exercicio

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.projeto.smartgymfront.ui.componentes.CardRegistro
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.ExercicioViewModel

@Composable
fun ExercicioListagem(
    viewModel: ExercicioViewModel,
    aoNovo: () -> Unit,
    aoEditar: (Long) -> Unit,
    aoVoltar: () -> Unit
) {
    Tela("Exercícios", aoVoltar) {
        Button(onClick = aoNovo, modifier = Modifier.fillMaxWidth()) {
            Text("Novo exercício")
        }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        if (viewModel.lista.isEmpty()) Text("Nenhum registro encontrado.")
        viewModel.lista.forEach { item ->
            val id = item.id ?: return@forEach
            CardRegistro(
                titulo = "#${item.id} - ${item.nome}",
                campos = listOf(
                    "Grupo muscular: ${item.grupoMuscular}",
                    "Séries x repetições: ${item.series} x ${item.repeticoes}",
                    "Carga sugerida: ${item.cargaSugerida?.let { "$it kg" } ?: "-"}",
                    "Precisa de acompanhamento: ${if (item.precisaAcompanhamento) "Sim" else "Não"}"
                ),
                aoEditar = { aoEditar(id) },
                aoApagar = { viewModel.apagar(id) }
            )
        }
    }
}
