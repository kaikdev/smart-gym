package edu.projeto.smartgymfront.ui.agendamento

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.AgendamentoViewModel

@Composable
fun AgendamentoFormulario(viewModel: AgendamentoViewModel, aoVoltar: () -> Unit) {
    val titulo = if (viewModel.idEdicao == null) "Cadastro de Agendamento" else "Editar agendamento #${viewModel.idEdicao}"

    Tela(titulo, aoVoltar) {
        CampoTexto("Tipo de aula", viewModel.tipoAula, { viewModel.tipoAula = it }, viewModel.erros["tipoAula"])
        CampoTexto("Instrutor", viewModel.instrutor, { viewModel.instrutor = it }, viewModel.erros["instrutor"])
        CampoTexto(
            "Data e hora",
            viewModel.dataHoraAgendada,
            { viewModel.dataHoraAgendada = it },
            viewModel.erros["dataHoraAgendada"],
            dica = "dd/mm/aaaa hh:mm"
        )
        CampoTexto(
            "Capacidade máxima",
            viewModel.capacidadeMaxima,
            { viewModel.capacidadeMaxima = it },
            viewModel.erros["capacidadeMaxima"],
            teclado = KeyboardType.Number
        )
        CampoCheckbox("Cancelado", viewModel.cancelado) { viewModel.cancelado = it }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        BotoesFormulario(
            aoGravar = { viewModel.gravar(aoConcluir = aoVoltar) },
            aoLimpar = viewModel::limpar,
            carregando = viewModel.carregando
        )
    }
}
