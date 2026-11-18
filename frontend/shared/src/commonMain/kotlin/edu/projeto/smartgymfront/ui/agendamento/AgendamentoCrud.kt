package edu.projeto.smartgymfront.ui.agendamento

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.projeto.smartgymfront.navegacao.NavegacaoCrud
import edu.projeto.smartgymfront.repositorio.AgendamentoRepositorioRemoto
import edu.projeto.smartgymfront.viewmodel.AgendamentoViewModel

@Composable
fun AgendamentoCrud(aoVoltar: () -> Unit) {
    val viewModel = viewModel { AgendamentoViewModel(AgendamentoRepositorioRemoto()) }

    NavegacaoCrud(
        aoSair = aoVoltar,
        listagem = { abrirFormulario ->
            AgendamentoListagem(
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
            AgendamentoFormulario(viewModel = viewModel, aoVoltar = voltarParaListagem)
        }
    )
}
