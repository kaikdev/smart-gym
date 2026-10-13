package edu.projeto.smartgymfront.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Usuario
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.ListaRegistros
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.ui.componentes.hoje
import edu.projeto.smartgymfront.ui.componentes.textoParaData
import edu.projeto.smartgymfront.ui.componentes.textoParaFloat
import kotlinx.coroutines.launch

@Composable
fun UsuarioForm(aoVoltar: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var dataNascimento by remember { mutableStateOf("") }
    var pesoAtual by remember { mutableStateOf("") }
    var ativo by remember { mutableStateOf(true) }

    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var usuarios by remember { mutableStateOf(listOf<Usuario>()) }
    val escopo = rememberCoroutineScope()

    suspend fun carregar() {
        try {
            usuarios = ApiClient.listar("api/usuarios")
        } catch (e: ApiException) {
            sucesso = false
            mensagem = e.mensagem
        }
    }

    LaunchedEffect(Unit) { carregar() }

    fun limpar() {
        nome = ""
        email = ""
        senha = ""
        dataNascimento = ""
        pesoAtual = ""
        ativo = true
        erros = emptyMap()
        mensagem = null
    }

    fun validar(): Usuario? {
        val novos = mutableMapOf<String, String>()
        if (nome.isBlank()) novos["nome"] = "O nome é obrigatório"
        else if (nome.length > 100) novos["nome"] = "O nome deve ter no máximo 100 caracteres"

        if (email.isBlank()) novos["email"] = "O e-mail é obrigatório"
        else if (!Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(email.trim())) novos["email"] = "E-mail inválido"
        else if (email.length > 150) novos["email"] = "O e-mail deve ter no máximo 150 caracteres"

        if (senha.isBlank()) novos["senha"] = "A senha é obrigatória"
        else if (senha.length !in 6..100) novos["senha"] = "A senha deve ter entre 6 e 100 caracteres"

        val data = if (dataNascimento.isBlank()) null else textoParaData(dataNascimento)
        if (dataNascimento.isNotBlank() && data == null) novos["dataNascimento"] = "Data inválida, use dd/mm/aaaa"
        else if (data != null && data >= hoje()) novos["dataNascimento"] = "A data de nascimento deve estar no passado"

        val peso = if (pesoAtual.isBlank()) null else textoParaFloat(pesoAtual)
        if (pesoAtual.isNotBlank() && peso == null) novos["pesoAtual"] = "Peso inválido"
        else if (peso != null && peso <= 0f) novos["pesoAtual"] = "O peso deve ser maior que zero"

        erros = novos
        if (novos.isNotEmpty()) return null
        return Usuario(
            nome = nome.trim(),
            email = email.trim(),
            senha = senha,
            dataNascimento = data,
            pesoAtual = peso,
            ativo = ativo
        )
    }

    fun gravar() {
        mensagem = null
        val usuario = validar() ?: return
        carregando = true
        escopo.launch {
            try {
                val gravado: Usuario = ApiClient.criar("api/usuarios", usuario)
                limpar()
                sucesso = true
                mensagem = "Usuário gravado com sucesso (id ${gravado.id})"
                carregar()
            } catch (e: ApiException) {
                erros = e.erros
                sucesso = false
                mensagem = e.mensagem
            } finally {
                carregando = false
            }
        }
    }

    Tela("Cadastro de Usuário", aoVoltar) {
        CampoTexto("Nome", nome, { nome = it }, erros["nome"])
        CampoTexto("E-mail", email, { email = it }, erros["email"], teclado = KeyboardType.Email)
        CampoTexto("Senha", senha, { senha = it }, erros["senha"], senha = true)
        CampoTexto(
            "Data de nascimento",
            dataNascimento,
            { dataNascimento = it },
            erros["dataNascimento"],
            teclado = KeyboardType.Number,
            dica = "dd/mm/aaaa"
        )
        CampoTexto("Peso atual (kg)", pesoAtual, { pesoAtual = it }, erros["pesoAtual"], teclado = KeyboardType.Decimal)
        CampoCheckbox("Ativo", ativo) { ativo = it }
        Mensagem(mensagem, sucesso)
        BotoesFormulario(aoGravar = ::gravar, aoLimpar = ::limpar, carregando = carregando)

        ListaRegistros(
            "Usuários cadastrados",
            usuarios.map {
                "#${it.id} - ${it.nome} (${it.email})\n" +
                    "Nascimento: ${dataParaTexto(it.dataNascimento).ifBlank { "-" }} | " +
                    "Peso: ${it.pesoAtual ?: "-"} kg | ${if (it.ativo) "Ativo" else "Inativo"}"
            }
        )
    }
}
