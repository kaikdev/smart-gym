package edu.projeto.smartgymfront.ui.plano

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.PlanoViewModel

@Composable
fun PlanoFormulario(viewModel: PlanoViewModel, aoVoltar: () -> Unit) {
    val titulo = if (viewModel.idEdicao == null) "Cadastro de Plano" else "Editar plano #${viewModel.idEdicao}"

    Tela(titulo, aoVoltar) {
        CampoTexto("Descrição", viewModel.descricao, { viewModel.descricao = it }, viewModel.erros["descricao"])
        CampoTexto("Valor mensal (R$)", viewModel.valorMensal, { viewModel.valorMensal = it }, viewModel.erros["valorMensal"], teclado = KeyboardType.Decimal)
        CampoTexto("Meses de fidelidade", viewModel.mesesFidelidade, { viewModel.mesesFidelidade = it }, viewModel.erros["mesesFidelidade"], teclado = KeyboardType.Number)
        CampoTexto("Taxa de matrícula (R$)", viewModel.taxaMatricula, { viewModel.taxaMatricula = it }, viewModel.erros["taxaMatricula"], teclado = KeyboardType.Decimal)
        CampoCheckbox("Promocional", viewModel.promocional) { viewModel.promocional = it }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        BotoesFormulario(
            aoGravar = { viewModel.gravar(aoConcluir = aoVoltar) },
            aoLimpar = viewModel::limpar,
            carregando = viewModel.carregando
        )
    }
}
