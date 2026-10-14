package edu.projeto.smartgymfront.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import edu.projeto.smartgymfront.TelaApp
import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.ui.componentes.Tela

@Composable
fun MenuScreen(aoAbrir: (TelaApp) -> Unit, aoSair: () -> Unit) {
    val opcoes = listOf(
        "Usuários" to TelaApp.USUARIO,
        "Planos" to TelaApp.PLANO,
        "Treinos" to TelaApp.TREINO,
        "Avaliações Físicas" to TelaApp.AVALIACAO_FISICA,
        "Agendamentos" to TelaApp.AGENDAMENTO,
        "Pagamentos" to TelaApp.PAGAMENTO
    )

    Tela("Smart Gym") {
        Text("Logado como: ${ApiClient.emailLogado ?: ""}")
        if (opcoes.isEmpty()) Text("Nenhum cadastro disponível ainda.")
        opcoes.forEach { (titulo, tela) ->
            Button(onClick = { aoAbrir(tela) }, modifier = Modifier.fillMaxWidth()) {
                Text(titulo)
            }
        }
        OutlinedButton(onClick = aoSair, modifier = Modifier.fillMaxWidth()) {
            Text("Sair")
        }
    }
}
