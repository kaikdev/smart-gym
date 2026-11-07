package edu.projeto.smartgymfront.ui.exercicio

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.ExercicioViewModel

@Composable
fun ExercicioFormulario(viewModel: ExercicioViewModel, aoVoltar: () -> Unit) {
    val titulo = if (viewModel.idEdicao == null) "Cadastro de Exercício" else "Editar exercício #${viewModel.idEdicao}"

    Tela(titulo, aoVoltar) {
        CampoTexto("Nome", viewModel.nome, { viewModel.nome = it }, viewModel.erros["nome"])
        CampoTexto("Grupo muscular", viewModel.grupoMuscular, { viewModel.grupoMuscular = it }, viewModel.erros["grupoMuscular"], dica = "ex.: Peito, Costas, Pernas")
        CampoTexto("Séries", viewModel.series, { viewModel.series = it }, viewModel.erros["series"], teclado = KeyboardType.Number)
        CampoTexto("Repetições", viewModel.repeticoes, { viewModel.repeticoes = it }, viewModel.erros["repeticoes"], teclado = KeyboardType.Number)
        CampoTexto("Carga sugerida (kg)", viewModel.cargaSugerida, { viewModel.cargaSugerida = it }, viewModel.erros["cargaSugerida"], teclado = KeyboardType.Decimal)
        CampoCheckbox("Precisa de acompanhamento", viewModel.precisaAcompanhamento) { viewModel.precisaAcompanhamento = it }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        BotoesFormulario(
            aoGravar = { viewModel.gravar(aoConcluir = aoVoltar) },
            aoLimpar = viewModel::limpar,
            carregando = viewModel.carregando
        )
    }
}
