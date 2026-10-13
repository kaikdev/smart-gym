package edu.projeto.smartgymfront.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun LoginScreen(aoEntrar: () -> Unit) {
    var criarConta by remember { mutableStateOf(false) }
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    val escopo = rememberCoroutineScope()

    fun validar(): Boolean {
        val novos = mutableMapOf<String, String>()
        if (criarConta && nome.isBlank()) novos["nome"] = "O nome é obrigatório"
        if (email.isBlank()) novos["email"] = "O e-mail é obrigatório"
        else if (!email.contains("@")) novos["email"] = "E-mail inválido"
        if (senha.isBlank()) novos["senha"] = "A senha é obrigatória"
        else if (criarConta && senha.length < 6) novos["senha"] = "A senha deve ter entre 6 e 100 caracteres"
        erros = novos
        return novos.isEmpty()
    }

    fun enviar() {
        mensagem = null
        if (!validar()) return
        carregando = true
        escopo.launch {
            try {
                if (criarConta) {
                    AuthApi.cadastrar(nome.trim(), email.trim(), senha)
                    criarConta = false
                    senha = ""
                    sucesso = true
                    mensagem = "Conta criada. Faça login."
                } else {
                    AuthApi.entrar(email.trim(), senha)
                    aoEntrar()
                }
            } catch (e: ApiException) {
                erros = e.erros
                sucesso = false
                mensagem = e.mensagem
            } finally {
                carregando = false
            }
        }
    }

    Tela(if (criarConta) "Criar conta" else "Smart Gym - Login") {
        if (criarConta) CampoTexto("Nome", nome, { nome = it }, erros["nome"])
        CampoTexto("E-mail", email, { email = it }, erros["email"], teclado = KeyboardType.Email)
        CampoTexto("Senha", senha, { senha = it }, erros["senha"], senha = true)
        Mensagem(mensagem, sucesso)
        Button(onClick = ::enviar, enabled = !carregando, modifier = Modifier.fillMaxWidth()) {
            Text(if (criarConta) "Cadastrar" else "Entrar")
        }
        if (carregando) CircularProgressIndicator()
        TextButton(onClick = {
            criarConta = !criarConta
            erros = emptyMap()
            mensagem = null
        }) {
            Text(if (criarConta) "Já tenho conta" else "Criar conta")
        }
    }
}
