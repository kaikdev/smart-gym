package edu.projeto.smartgymfront.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.projeto.smartgymfront.navegacao.Rota
import edu.projeto.smartgymfront.sessao.SessaoUsuario
import edu.projeto.smartgymfront.ui.componentes.Tela

@Composable
fun MenuScreen(aoAbrir: (Rota) -> Unit, aoSair: () -> Unit) {
    val opcoes = listOf(
        "Usuários" to Rota.Usuarios,
        "Planos" to Rota.Planos,
        "Treinos" to Rota.Treinos,
        "Avaliações Físicas" to Rota.Avaliacoes,
        "Agendamentos" to Rota.Agendamentos,
        "Pagamentos" to Rota.Pagamentos,
        "Exercícios" to Rota.Exercicios,
        "Equipamentos" to Rota.Equipamentos
    )

    Tela("Smart Gym") {
        Text("Logado como: ${SessaoUsuario.email ?: ""}")
        opcoes.forEach { (titulo, rota) ->
            Button(onClick = { aoAbrir(rota) }, modifier = Modifier.fillMaxWidth()) {
                Text(titulo)
            }
        }
        OutlinedButton(onClick = aoSair, modifier = Modifier.fillMaxWidth()) {
            Text("Sair")
        }
    }
}
