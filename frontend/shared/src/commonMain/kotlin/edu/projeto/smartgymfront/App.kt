package edu.projeto.smartgymfront

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.projeto.smartgymfront.api.AuthApi
import edu.projeto.smartgymfront.ui.AgendamentoForm
import edu.projeto.smartgymfront.ui.AvaliacaoFisicaForm
import edu.projeto.smartgymfront.ui.LoginScreen
import edu.projeto.smartgymfront.ui.MenuScreen
import edu.projeto.smartgymfront.ui.PagamentoForm
import edu.projeto.smartgymfront.ui.PlanoForm
import edu.projeto.smartgymfront.ui.TreinoForm
import edu.projeto.smartgymfront.ui.UsuarioForm

enum class TelaApp {
    LOGIN,
    MENU,
    USUARIO,
    PLANO,
    TREINO,
    AVALIACAO_FISICA,
    AGENDAMENTO,
    PAGAMENTO
}

@Composable
@Preview
fun App() {
    var tela by remember { mutableStateOf(TelaApp.LOGIN) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Box(modifier = Modifier.widthIn(max = 600.dp)) {
                    when (tela) {
                        TelaApp.LOGIN -> LoginScreen(aoEntrar = { tela = TelaApp.MENU })
                        TelaApp.MENU -> MenuScreen(
                            aoAbrir = { tela = it },
                            aoSair = {
                                AuthApi.sair()
                                tela = TelaApp.LOGIN
                            }
                        )
                        TelaApp.USUARIO -> UsuarioForm(aoVoltar = { tela = TelaApp.MENU })
                        TelaApp.PLANO -> PlanoForm(aoVoltar = { tela = TelaApp.MENU })
                        TelaApp.TREINO -> TreinoForm(aoVoltar = { tela = TelaApp.MENU })
                        TelaApp.AVALIACAO_FISICA -> AvaliacaoFisicaForm(aoVoltar = { tela = TelaApp.MENU })
                        TelaApp.AGENDAMENTO -> AgendamentoForm(aoVoltar = { tela = TelaApp.MENU })
                        TelaApp.PAGAMENTO -> PagamentoForm(aoVoltar = { tela = TelaApp.MENU })
                    }
                }
            }
        }
    }
}
