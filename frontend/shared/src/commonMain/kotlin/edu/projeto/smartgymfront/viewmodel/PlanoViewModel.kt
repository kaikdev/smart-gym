package edu.projeto.smartgymfront.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Plano
import edu.projeto.smartgymfront.repositorio.PlanoRepositorioRemoto
import edu.projeto.smartgymfront.ui.componentes.textoParaDouble
import edu.projeto.smartgymfront.ui.componentes.textoParaInt
import kotlinx.coroutines.launch

class PlanoViewModel(private val repositorio: PlanoRepositorioRemoto) : ViewModel() {

    var descricao by mutableStateOf("")
    var valorMensal by mutableStateOf("")
    var mesesFidelidade by mutableStateOf("0")
    var taxaMatricula by mutableStateOf("0")
    var promocional by mutableStateOf(false)

    var lista by mutableStateOf(listOf<Plano>())
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
        descricao = ""
        valorMensal = ""
        mesesFidelidade = "0"
        taxaMatricula = "0"
        promocional = false
        idEdicao = null
        erros = emptyMap()
        mensagem = null
    }

    fun editar(id: Long) {
        limpar()
        viewModelScope.launch {
            try {
                val registro = repositorio.buscarPorId(id)
                descricao = registro.descricao
                valorMensal = registro.valorMensal.toString()
                mesesFidelidade = registro.mesesFidelidade.toString()
                taxaMatricula = registro.taxaMatricula.toString()
                promocional = registro.promocional
                idEdicao = id
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun gravar(aoConcluir: () -> Unit) {
        mensagem = null
        val plano = validar() ?: return
        carregando = true
        viewModelScope.launch {
            try {
                val id = idEdicao
                val gravado = if (id == null) repositorio.criar(plano) else repositorio.atualizar(id, plano)
                limpar()
                sucesso = true
                mensagem = if (id == null) "Plano gravado com sucesso (id ${gravado.id})"
                else "Plano atualizado com sucesso (id ${gravado.id})"
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
                mensagem = "Plano apagado com sucesso (id $id)"
                carregar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    private fun validar(): Plano? {
        val novos = mutableMapOf<String, String>()
        if (descricao.isBlank()) novos["descricao"] = "A descrição é obrigatória"
        else if (descricao.length > 100) novos["descricao"] = "A descrição deve ter no máximo 100 caracteres"

        val valor = textoParaDouble(valorMensal)
        if (valorMensal.isBlank()) novos["valorMensal"] = "O valor mensal é obrigatório"
        else if (valor == null) novos["valorMensal"] = "Valor inválido"
        else if (valor <= 0.0) novos["valorMensal"] = "O valor mensal deve ser maior que zero"

        val meses = textoParaInt(mesesFidelidade.ifBlank { "0" })
        if (meses == null) novos["mesesFidelidade"] = "Informe um número inteiro"
        else if (meses < 0) novos["mesesFidelidade"] = "Os meses de fidelidade não podem ser negativos"

        val taxa = textoParaDouble(taxaMatricula.ifBlank { "0" })
        if (taxa == null) novos["taxaMatricula"] = "Valor inválido"
        else if (taxa < 0.0) novos["taxaMatricula"] = "A taxa de matrícula não pode ser negativa"

        erros = novos
        if (novos.isNotEmpty() || valor == null || meses == null || taxa == null) return null
        return Plano(
            descricao = descricao.trim(),
            valorMensal = valor,
            mesesFidelidade = meses,
            taxaMatricula = taxa,
            promocional = promocional
        )
    }

    private fun mostrarErro(e: ApiException) {
        sucesso = false
        mensagem = e.mensagem
    }
}
