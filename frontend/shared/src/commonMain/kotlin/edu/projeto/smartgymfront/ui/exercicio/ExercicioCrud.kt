package edu.projeto.smartgymfront.ui.exercicio

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.projeto.smartgymfront.navegacao.NavegacaoCrud
import edu.projeto.smartgymfront.repositorio.ExercicioRepositorioRemoto
import edu.projeto.smartgymfront.viewmodel.ExercicioViewModel

@Composable
fun ExercicioCrud(aoVoltar: () -> Unit) {
    val viewModel = viewModel { ExercicioViewModel(ExercicioRepositorioRemoto()) }

    NavegacaoCrud(
        aoSair = aoVoltar,
        listagem = { abrirFormulario ->
            ExercicioListagem(
                viewModel = viewModel,
                aoNovo = {
                    viewModel.limpar()
                    abrirFormulario()
                },
                aoEditar = { id ->
                    viewModel.editar(id)
                    abrirFormulario()
                },
                aoVoltar = aoVoltar
            )
        },
        formulario = { voltarParaListagem ->
            ExercicioFormulario(viewModel = viewModel, aoVoltar = voltarParaListagem)
        }
    )
}
