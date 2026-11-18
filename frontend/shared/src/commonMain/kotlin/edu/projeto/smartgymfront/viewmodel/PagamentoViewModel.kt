package edu.projeto.smartgymfront.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Pagamento
import edu.projeto.smartgymfront.repositorio.PagamentoRepositorioRemoto
import edu.projeto.smartgymfront.ui.componentes.textoParaDouble
import kotlinx.coroutines.launch

class PagamentoViewModel(private val repositorio: PagamentoRepositorioRemoto) : ViewModel() {

    var metodo by mutableStateOf("")
    var codigoTransacao by mutableStateOf("")
    var valorFinal by mutableStateOf("")
    var confirmado by mutableStateOf(false)

    var lista by mutableStateOf(listOf<Pagamento>())
        private set
    var idEdicao by mutableStateOf<Long?>(null)
        private set
    var erros by mutableStateOf(mapOf<String, String>())
        private set
    var mensagem by mutableStateOf<String?>(null)
        private set
    var sucesso by mutableStateOf(false)
        private set
    var carregando by mutableStateOf(false)
        private set

    init {
        carregar()
    }

    fun carregar() {
        viewModelScope.launch {
            try {
                lista = repositorio.listar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun limpar() {
        metodo = ""
        codigoTransacao = ""
        valorFinal = ""
        confirmado = false
        idEdicao = null
        erros = emptyMap()
        mensagem = null
    }

    fun editar(id: Long) {
        limpar()
        viewModelScope.launch {
            try {
                val registro = repositorio.buscarPorId(id)
                metodo = registro.metodo
                codigoTransacao = registro.codigoTransacao ?: ""
                valorFinal = registro.valorFinal.toString()
                confirmado = registro.confirmado
                idEdicao = id
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun gravar(aoConcluir: () -> Unit) {
        mensagem = null
        val pagamento = validar() ?: return
        carregando = true
        viewModelScope.launch {
            try {
                val id = idEdicao
                val gravado = if (id == null) repositorio.criar(pagamento) else repositorio.atualizar(id, pagamento)
                limpar()
                sucesso = true
                mensagem = if (id == null) "Pagamento gravado com sucesso (id ${gravado.id})"
                else "Pagamento atualizado com sucesso (id ${gravado.id})"
                carregar()
                aoConcluir()
            } catch (e: ApiException) {
                erros = e.erros
                mostrarErro(e)
            } finally {
                carregando = false
            }
        }
    }

    fun apagar(id: Long) {
        viewModelScope.launch {
            try {
                repositorio.remover(id)
                if (idEdicao == id) limpar()
                sucesso = true
                mensagem = "Pagamento apagado com sucesso (id $id)"
                carregar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    private fun validar(): Pagamento? {
        val novos = mutableMapOf<String, String>()
        if (metodo.isBlank()) novos["metodo"] = "O método de pagamento é obrigatório"
        else if (metodo.length > 40) novos["metodo"] = "O método deve ter no máximo 40 caracteres"

        if (codigoTransacao.length > 100) novos["codigoTransacao"] = "O código da transação deve ter no máximo 100 caracteres"

        val valor = textoParaDouble(valorFinal)
        if (valorFinal.isBlank()) novos["valorFinal"] = "O valor final é obrigatório"
        else if (valor == null) novos["valorFinal"] = "Valor inválido"
        else if (valor <= 0.0) novos["valorFinal"] = "O valor final deve ser maior que zero"

        erros = novos
        if (novos.isNotEmpty() || valor == null) return null
        return Pagamento(
            metodo = metodo.trim(),
            codigoTransacao = codigoTransacao.trim().ifBlank { null },
            valorFinal = valor,
            confirmado = confirmado
        )
    }

    private fun mostrarErro(e: ApiException) {
        sucesso = false
        mensagem = e.mensagem
    }
}
