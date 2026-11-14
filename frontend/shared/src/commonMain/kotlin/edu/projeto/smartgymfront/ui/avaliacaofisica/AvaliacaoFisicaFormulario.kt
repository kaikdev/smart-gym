package edu.projeto.smartgymfront.ui.avaliacaofisica

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.AvaliacaoFisicaViewModel

@Composable
fun AvaliacaoFisicaFormulario(viewModel: AvaliacaoFisicaViewModel, aoVoltar: () -> Unit) {
    val titulo = if (viewModel.idEdicao == null) "Cadastro de Avaliação Física" else "Editar avaliação física #${viewModel.idEdicao}"

    Tela(titulo, aoVoltar) {
        CampoTexto("Avaliador", viewModel.avaliador, { viewModel.avaliador = it }, viewModel.erros["avaliador"])
        CampoTexto(
            "Data da avaliação",
            viewModel.dataAvaliacao,
            { viewModel.dataAvaliacao = it },
            viewModel.erros["dataAvaliacao"],
            teclado = KeyboardType.Number,
            dica = "dd/mm/aaaa"
        )
        CampoTexto("Altura (m)", viewModel.altura, { viewModel.altura = it }, viewModel.erros["altura"], teclado = KeyboardType.Decimal, dica = "ex.: 1,75")
        CampoTexto("IMC", viewModel.imc, { viewModel.imc = it }, viewModel.erros["imc"], teclado = KeyboardType.Decimal)
        CampoTexto(
            "Percentual de gordura (%)",
            viewModel.percentualGordura,
            { viewModel.percentualGordura = it },
            viewModel.erros["percentualGordura"],
            teclado = KeyboardType.Decimal
        )
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        BotoesFormulario(
            aoGravar = { viewModel.gravar(aoConcluir = aoVoltar) },
            aoLimpar = viewModel::limpar,
            carregando = viewModel.carregando
        )
    }
}
