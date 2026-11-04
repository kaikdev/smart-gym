package edu.projeto.smartgymfront.navegacao

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

sealed interface RotaCrud {
    data object Listagem : RotaCrud
    data object Formulario : RotaCrud
}

@Composable
fun NavegacaoCrud(
    aoSair: () -> Unit,
    listagem: @Composable (abrirFormulario: () -> Unit) -> Unit,
    formulario: @Composable (voltarParaListagem: () -> Unit) -> Unit
) {
    val pilha = remember { mutableStateListOf<RotaCrud>(RotaCrud.Listagem) }

    fun abrirFormulario() {
        if (pilha.last() != RotaCrud.Formulario) pilha.add(RotaCrud.Formulario)
    }

    fun voltar() {
        if (pilha.size > 1) pilha.removeLastOrNull() else aoSair()
    }

    NavDisplay(
        backStack = pilha,
        onBack = ::voltar,
        entryProvider = entryProvider {
            entry<RotaCrud.Listagem> { listagem(::abrirFormulario) }
            entry<RotaCrud.Formulario> { formulario(::voltar) }
        }
    )
}
