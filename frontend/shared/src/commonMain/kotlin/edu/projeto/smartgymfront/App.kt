package edu.projeto.smartgymfront

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import edu.projeto.smartgymfront.api.AuthApi
import edu.projeto.smartgymfront.navegacao.Rota
import edu.projeto.smartgymfront.sessao.SessaoUsuario
import edu.projeto.smartgymfront.ui.AgendamentoForm
import edu.projeto.smartgymfront.ui.AvaliacaoFisicaForm
import edu.projeto.smartgymfront.ui.EquipamentoForm
import edu.projeto.smartgymfront.ui.ExercicioForm
import edu.projeto.smartgymfront.ui.LoginScreen
import edu.projeto.smartgymfront.ui.MenuScreen
import edu.projeto.smartgymfront.ui.PagamentoForm
import edu.projeto.smartgymfront.ui.PlanoForm
import edu.projeto.smartgymfront.ui.RegistroScreen
import edu.projeto.smartgymfront.ui.TreinoForm
import edu.projeto.smartgymfront.ui.usuario.UsuarioCrud

@Composable
@Preview
fun App() {
    val pilha = remember { mutableStateListOf<Rota>(Rota.Login) }

    fun abrir(rota: Rota) {
        pilha.add(rota)
    }

    fun reiniciar(rota: Rota) {
        pilha.add(rota)
        repeat(pilha.size - 1) { pilha.removeAt(0) }
    }

    fun voltar() {
        if (pilha.size > 1) pilha.removeLastOrNull()
    }

    LaunchedEffect(SessaoUsuario.autenticado) {
        val telaPublica = pilha.last() == Rota.Login || pilha.last() == Rota.Registro
        if (!SessaoUsuario.autenticado && !telaPublica) reiniciar(Rota.Login)
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavDisplay(
                backStack = pilha,
                onBack = ::voltar,
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {
                    entry<Rota.Login> {
                        LoginScreen(
                            aoEntrar = { reiniciar(Rota.Menu) },
                            aoIrParaRegistro = { reiniciar(Rota.Registro) }
                        )
                    }
                    entry<Rota.Registro> {
                        RegistroScreen(
                            aoRegistrar = { reiniciar(Rota.Menu) },
                            aoIrParaLogin = { reiniciar(Rota.Login) }
                        )
                    }
                    entry<Rota.Menu> {
                        MenuScreen(aoAbrir = ::abrir, aoSair = AuthApi::sair)
                    }
                    entry<Rota.Usuarios> { UsuarioCrud(aoVoltar = ::voltar) }
                    entry<Rota.Planos> { PlanoForm(aoVoltar = ::voltar) }
                    entry<Rota.Exercicios> { ExercicioForm(aoVoltar = ::voltar) }
                    entry<Rota.Equipamentos> { EquipamentoForm(aoVoltar = ::voltar) }
                    entry<Rota.Treinos> { TreinoForm(aoVoltar = ::voltar) }
                    entry<Rota.Avaliacoes> { AvaliacaoFisicaForm(aoVoltar = ::voltar) }
                    entry<Rota.Agendamentos> { AgendamentoForm(aoVoltar = ::voltar) }
                    entry<Rota.Pagamentos> { PagamentoForm(aoVoltar = ::voltar) }
                }
            )
        }
    }
}
