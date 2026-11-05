package edu.projeto.smartgymfront.ui.usuario

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.projeto.smartgymfront.navegacao.NavegacaoCrud
import edu.projeto.smartgymfront.repositorio.UsuarioRepositorioRemoto
import edu.projeto.smartgymfront.viewmodel.UsuarioViewModel

@Composable
fun UsuarioCrud(aoVoltar: () -> Unit) {
    val viewModel = viewModel { UsuarioViewModel(UsuarioRepositorioRemoto()) }

    NavegacaoCrud(
        aoSair = aoVoltar,
        listagem = { abrirFormulario ->
            UsuarioListagem(
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
            UsuarioFormulario(viewModel = viewModel, aoVoltar = voltarParaListagem)
        }
    )
}
