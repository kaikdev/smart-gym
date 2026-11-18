package edu.projeto.smartgymfront.ui.pagamento

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.projeto.smartgymfront.navegacao.NavegacaoCrud
import edu.projeto.smartgymfront.repositorio.PagamentoRepositorioRemoto
import edu.projeto.smartgymfront.viewmodel.PagamentoViewModel

@Composable
fun PagamentoCrud(aoVoltar: () -> Unit) {
    val viewModel = viewModel { PagamentoViewModel(PagamentoRepositorioRemoto()) }

    NavegacaoCrud(
        aoSair = aoVoltar,
        listagem = { abrirFormulario ->
            PagamentoListagem(
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
            PagamentoFormulario(viewModel = viewModel, aoVoltar = voltarParaListagem)
        }
    )
}
