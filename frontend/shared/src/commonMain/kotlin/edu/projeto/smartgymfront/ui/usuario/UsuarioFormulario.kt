package edu.projeto.smartgymfront.ui.usuario

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.viewmodel.UsuarioViewModel

@Composable
fun UsuarioFormulario(viewModel: UsuarioViewModel, aoVoltar: () -> Unit) {
    val titulo = if (viewModel.idEdicao == null) "Cadastro de Usuário" else "Editar usuário #${viewModel.idEdicao}"

    Tela(titulo, aoVoltar) {
        CampoTexto("Nome", viewModel.nome, { viewModel.nome = it }, viewModel.erros["nome"])
        CampoTexto("E-mail", viewModel.email, { viewModel.email = it }, viewModel.erros["email"], teclado = KeyboardType.Email)
        CampoTexto(
            "Senha",
            viewModel.senha,
            { viewModel.senha = it },
            viewModel.erros["senha"],
            senha = true,
            dica = if (viewModel.idEdicao != null) "Digite a senha novamente" else null
        )
        CampoTexto(
            "Data de nascimento",
            viewModel.dataNascimento,
            { viewModel.dataNascimento = it },
            viewModel.erros["dataNascimento"],
            teclado = KeyboardType.Number,
            dica = "dd/mm/aaaa"
        )
        CampoTexto("Peso atual (kg)", viewModel.pesoAtual, { viewModel.pesoAtual = it }, viewModel.erros["pesoAtual"], teclado = KeyboardType.Decimal)
        CampoCheckbox("Ativo", viewModel.ativo) { viewModel.ativo = it }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        BotoesFormulario(
            aoGravar = { viewModel.gravar(aoConcluir = aoVoltar) },
            aoLimpar = viewModel::limpar,
            carregando = viewModel.carregando
        )
    }
}
