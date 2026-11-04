package edu.projeto.smartgymfront.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.api.AuthApi
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import kotlinx.coroutines.launch

@Composable
fun RegistroScreen(aoRegistrar: () -> Unit, aoIrParaLogin: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmacaoSenha by remember { mutableStateOf("") }
    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var carregando by remember { mutableStateOf(false) }
    val escopo = rememberCoroutineScope()

    fun validar(): Boolean {
        val novos = mutableMapOf<String, String>()
        if (nome.isBlank()) novos["nome"] = "O nome é obrigatório"
        else if (nome.length > 100) novos["nome"] = "O nome deve ter no máximo 100 caracteres"
        if (email.isBlank()) novos["email"] = "O e-mail é obrigatório"
        else if (!Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(email.trim())) novos["email"] = "E-mail inválido"
        if (senha.isBlank()) novos["senha"] = "A senha é obrigatória"
        else if (senha.length !in 6..100) novos["senha"] = "A senha deve ter entre 6 e 100 caracteres"
        if (confirmacaoSenha.isBlank()) novos["confirmacaoSenha"] = "Confirme a senha"
        else if (confirmacaoSenha != senha) novos["confirmacaoSenha"] = "As senhas não conferem"
        erros = novos
        return novos.isEmpty()
    }

    fun registrar() {
        mensagem = null
        if (!validar()) return
        carregando = true
        escopo.launch {
            try {
                AuthApi.registrar(nome.trim(), email.trim(), senha)
                aoRegistrar()
            } catch (e: ApiException) {
                erros = e.erros
                mensagem = e.mensagem
            } finally {
                carregando = false
            }
        }
    }

    Tela("Smart Gym - Registro") {
        CampoTexto("Nome", nome, { nome = it }, erros["nome"])
        CampoTexto("E-mail", email, { email = it }, erros["email"], teclado = KeyboardType.Email)
        CampoTexto("Senha", senha, { senha = it }, erros["senha"], senha = true)
        CampoTexto(
            "Confirmação da senha",
            confirmacaoSenha,
            { confirmacaoSenha = it },
            erros["confirmacaoSenha"],
            senha = true
        )
        Mensagem(mensagem, false)
        Button(onClick = ::registrar, enabled = !carregando, modifier = Modifier.fillMaxWidth()) {
            Text("Registrar")
        }
        OutlinedButton(onClick = aoIrParaLogin, enabled = !carregando, modifier = Modifier.fillMaxWidth()) {
            Text("Ir para o login")
        }
        if (carregando) CircularProgressIndicator()
    }
}
