package edu.projeto.smartgymfront.ui.treino

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.projeto.smartgymfront.navegacao.NavegacaoCrud
import edu.projeto.smartgymfront.repositorio.TreinoRepositorioRemoto
import edu.projeto.smartgymfront.viewmodel.TreinoViewModel

@Composable
fun TreinoCrud(aoVoltar: () -> Unit) {
    val viewModel = viewModel { TreinoViewModel(TreinoRepositorioRemoto()) }

    NavegacaoCrud(
        aoSair = aoVoltar,
        listagem = { abrirFormulario ->
            TreinoListagem(
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
            TreinoFormulario(viewModel = viewModel, aoVoltar = voltarParaListagem)
        }
    )
}
