package edu.projeto.smartgymfront.ui.plano

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.projeto.smartgymfront.navegacao.NavegacaoCrud
import edu.projeto.smartgymfront.repositorio.PlanoRepositorioRemoto
import edu.projeto.smartgymfront.viewmodel.PlanoViewModel

@Composable
fun PlanoCrud(aoVoltar: () -> Unit) {
    val viewModel = viewModel { PlanoViewModel(PlanoRepositorioRemoto()) }

    NavegacaoCrud(
        aoSair = aoVoltar,
        listagem = { abrirFormulario ->
            PlanoListagem(
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
            PlanoFormulario(viewModel = viewModel, aoVoltar = voltarParaListagem)
        }
    )
}
