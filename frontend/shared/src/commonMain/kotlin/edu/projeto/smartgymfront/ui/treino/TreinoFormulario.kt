package edu.projeto.smartgymfront.ui.treino

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.TreinoViewModel

@Composable
fun TreinoFormulario(viewModel: TreinoViewModel, aoVoltar: () -> Unit) {
    val titulo = if (viewModel.idEdicao == null) "Cadastro de Treino" else "Editar treino #${viewModel.idEdicao}"

    Tela(titulo, aoVoltar) {
        CampoTexto("Título", viewModel.titulo, { viewModel.titulo = it }, viewModel.erros["titulo"])
        CampoTexto("Objetivo", viewModel.objetivo, { viewModel.objetivo = it }, viewModel.erros["objetivo"])
        CampoTexto(
            "Duração estimada (min)",
            viewModel.duracaoEstimadaMin,
            { viewModel.duracaoEstimadaMin = it },
            viewModel.erros["duracaoEstimadaMin"],
            teclado = KeyboardType.Number
        )
        CampoCheckbox("Público", viewModel.publico) { viewModel.publico = it }
        Text("A data de criação é preenchida automaticamente ao gravar.")
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        BotoesFormulario(
            aoGravar = { viewModel.gravar(aoConcluir = aoVoltar) },
            aoLimpar = viewModel::limpar,
            carregando = viewModel.carregando
        )
    }
}
