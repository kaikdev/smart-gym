package edu.projeto.smartgymfront.ui.avaliacaofisica

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.projeto.smartgymfront.navegacao.NavegacaoCrud
import edu.projeto.smartgymfront.repositorio.AvaliacaoFisicaRepositorioRemoto
import edu.projeto.smartgymfront.viewmodel.AvaliacaoFisicaViewModel

@Composable
fun AvaliacaoFisicaCrud(aoVoltar: () -> Unit) {
    val viewModel = viewModel { AvaliacaoFisicaViewModel(AvaliacaoFisicaRepositorioRemoto()) }

    NavegacaoCrud(
        aoSair = aoVoltar,
        listagem = { abrirFormulario ->
            AvaliacaoFisicaListagem(
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
            AvaliacaoFisicaFormulario(viewModel = viewModel, aoVoltar = voltarParaListagem)
        }
    )
}
