package edu.projeto.smartgymfront.ui.equipamento

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.EquipamentoViewModel

@Composable
fun EquipamentoFormulario(viewModel: EquipamentoViewModel, aoVoltar: () -> Unit) {
    val titulo = if (viewModel.idEdicao == null) "Cadastro de Equipamento" else "Editar equipamento #${viewModel.idEdicao}"

    Tela(titulo, aoVoltar) {
        CampoTexto("Nome", viewModel.nome, { viewModel.nome = it }, viewModel.erros["nome"])
        CampoTexto("Fabricante", viewModel.fabricante, { viewModel.fabricante = it }, viewModel.erros["fabricante"])
        CampoTexto(
            "Data de compra",
            viewModel.dataCompra,
            { viewModel.dataCompra = it },
            viewModel.erros["dataCompra"],
            teclado = KeyboardType.Number,
            dica = "dd/mm/aaaa"
        )
        CampoTexto("Peso suportado (kg)", viewModel.pesoSuportado, { viewModel.pesoSuportado = it }, viewModel.erros["pesoSuportado"], teclado = KeyboardType.Decimal)
        CampoTexto("Código do sensor IoT", viewModel.codigoSensorIot, { viewModel.codigoSensorIot = it }, viewModel.erros["codigoSensorIot"])
        CampoCheckbox("Em uso (sensor IoT)", viewModel.emUsoIot) { viewModel.emUsoIot = it }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        BotoesFormulario(
            aoGravar = { viewModel.gravar(aoConcluir = aoVoltar) },
            aoLimpar = viewModel::limpar,
            carregando = viewModel.carregando
        )
    }
}
