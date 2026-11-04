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
fun LoginScreen(aoEntrar: () -> Unit, aoIrParaRegistro: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var carregando by remember { mutableStateOf(false) }
    val escopo = rememberCoroutineScope()

    fun validar(): Boolean {
        val novos = mutableMapOf<String, String>()
        if (email.isBlank()) novos["email"] = "O e-mail é obrigatório"
        else if (!email.contains("@")) novos["email"] = "E-mail inválido"
        if (senha.isBlank()) novos["senha"] = "A senha é obrigatória"
        erros = novos
        return novos.isEmpty()
    }

    fun entrar() {
        mensagem = null
        if (!validar()) return
        carregando = true
        escopo.launch {
            try {
                AuthApi.entrar(email.trim(), senha)
                aoEntrar()
            } catch (e: ApiException) {
                erros = e.erros
                mensagem = e.mensagem
            } finally {
                carregando = false
            }
        }
    }

    Tela("Smart Gym - Login") {
        CampoTexto("E-mail", email, { email = it }, erros["email"], teclado = KeyboardType.Email)
        CampoTexto("Senha", senha, { senha = it }, erros["senha"], senha = true)
        Mensagem(mensagem, false)
        Button(onClick = ::entrar, enabled = !carregando, modifier = Modifier.fillMaxWidth()) {
            Text("Entrar")
        }
        OutlinedButton(onClick = aoIrParaRegistro, enabled = !carregando, modifier = Modifier.fillMaxWidth()) {
            Text("Ir para o registro")
        }
        if (carregando) CircularProgressIndicator()
    }
}
