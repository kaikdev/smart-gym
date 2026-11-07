package edu.projeto.smartgymfront.ui.equipamento

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.projeto.smartgymfront.navegacao.NavegacaoCrud
import edu.projeto.smartgymfront.repositorio.EquipamentoRepositorioRemoto
import edu.projeto.smartgymfront.viewmodel.EquipamentoViewModel

@Composable
fun EquipamentoCrud(aoVoltar: () -> Unit) {
    val viewModel = viewModel { EquipamentoViewModel(EquipamentoRepositorioRemoto()) }

    NavegacaoCrud(
        aoSair = aoVoltar,
        listagem = { abrirFormulario ->
            EquipamentoListagem(
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
            EquipamentoFormulario(viewModel = viewModel, aoVoltar = voltarParaListagem)
        }
    )
}
