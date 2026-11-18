package edu.projeto.smartgymfront.ui.pagamento

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.PagamentoViewModel

@Composable
fun PagamentoFormulario(viewModel: PagamentoViewModel, aoVoltar: () -> Unit) {
    val titulo = if (viewModel.idEdicao == null) "Cadastro de Pagamento" else "Editar pagamento #${viewModel.idEdicao}"

    Tela(titulo, aoVoltar) {
        CampoTexto("Método", viewModel.metodo, { viewModel.metodo = it }, viewModel.erros["metodo"], dica = "PIX, Cartão de crédito, Boleto, Dinheiro")
        CampoTexto("Código da transação", viewModel.codigoTransacao, { viewModel.codigoTransacao = it }, viewModel.erros["codigoTransacao"])
        CampoTexto("Valor final (R$)", viewModel.valorFinal, { viewModel.valorFinal = it }, viewModel.erros["valorFinal"], teclado = KeyboardType.Decimal)
        CampoCheckbox("Confirmado", viewModel.confirmado) { viewModel.confirmado = it }
        Text("A data e hora de processamento são preenchidas automaticamente ao gravar.")
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        BotoesFormulario(
            aoGravar = { viewModel.gravar(aoConcluir = aoVoltar) },
            aoLimpar = viewModel::limpar,
            carregando = viewModel.carregando
        )
    }
}
