package edu.projeto.smartgymfront.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.AvaliacaoFisica
import edu.projeto.smartgymfront.repositorio.AvaliacaoFisicaRepositorioRemoto
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.ui.componentes.hoje
import edu.projeto.smartgymfront.ui.componentes.textoParaData
import edu.projeto.smartgymfront.ui.componentes.textoParaDouble
import edu.projeto.smartgymfront.ui.componentes.textoParaFloat
import kotlinx.coroutines.launch

class AvaliacaoFisicaViewModel(private val repositorio: AvaliacaoFisicaRepositorioRemoto) : ViewModel() {

    var avaliador by mutableStateOf("")
    var dataAvaliacao by mutableStateOf("")
    var altura by mutableStateOf("")
    var imc by mutableStateOf("")
    var percentualGordura by mutableStateOf("")

    var lista by mutableStateOf(listOf<AvaliacaoFisica>())
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
        avaliador = ""
        dataAvaliacao = ""
        altura = ""
        imc = ""
        percentualGordura = ""
        idEdicao = null
        erros = emptyMap()
        mensagem = null
    }

    fun editar(id: Long) {
        limpar()
        viewModelScope.launch {
            try {
                val registro = repositorio.buscarPorId(id)
                avaliador = registro.avaliador
                dataAvaliacao = dataParaTexto(registro.dataAvaliacao)
                altura = registro.altura.toString()
                imc = registro.imc.toString()
                percentualGordura = registro.percentualGordura?.toString() ?: ""
                idEdicao = id
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun gravar(aoConcluir: () -> Unit) {
        mensagem = null
        val avaliacao = validar() ?: return
        carregando = true
        viewModelScope.launch {
            try {
                val id = idEdicao
                val gravada = if (id == null) repositorio.criar(avaliacao) else repositorio.atualizar(id, avaliacao)
                limpar()
                sucesso = true
                mensagem = if (id == null) "Avaliação gravada com sucesso (id ${gravada.id})"
                else "Avaliação atualizada com sucesso (id ${gravada.id})"
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
                mensagem = "Avaliação apagada com sucesso (id $id)"
                carregar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    private fun validar(): AvaliacaoFisica? {
        val novos = mutableMapOf<String, String>()
        if (avaliador.isBlank()) novos["avaliador"] = "O avaliador é obrigatório"
        else if (avaliador.length > 100) novos["avaliador"] = "O nome do avaliador deve ter no máximo 100 caracteres"

        val data = textoParaData(dataAvaliacao)
        if (dataAvaliacao.isBlank()) novos["dataAvaliacao"] = "A data da avaliação é obrigatória"
        else if (data == null) novos["dataAvaliacao"] = "Data inválida, use dd/mm/aaaa"
        else if (data > hoje()) novos["dataAvaliacao"] = "A data da avaliação não pode estar no futuro"

        val alturaValor = textoParaFloat(altura)
        if (altura.isBlank()) novos["altura"] = "A altura é obrigatória"
        else if (alturaValor == null) novos["altura"] = "Altura inválida"
        else if (alturaValor <= 0f) novos["altura"] = "A altura deve ser maior que zero"

        val imcValor = textoParaFloat(imc)
        if (imc.isBlank()) novos["imc"] = "O IMC é obrigatório"
        else if (imcValor == null) novos["imc"] = "IMC inválido"
        else if (imcValor <= 0f) novos["imc"] = "O IMC deve ser maior que zero"

        val gordura = if (percentualGordura.isBlank()) null else textoParaDouble(percentualGordura)
        if (percentualGordura.isNotBlank() && gordura == null) novos["percentualGordura"] = "Percentual inválido"
        else if (gordura != null && gordura !in 0.0..100.0) novos["percentualGordura"] = "O percentual de gordura deve estar entre 0 e 100"

        erros = novos
        if (novos.isNotEmpty() || data == null || alturaValor == null || imcValor == null) return null
        return AvaliacaoFisica(
            avaliador = avaliador.trim(),
            dataAvaliacao = data,
            altura = alturaValor,
            imc = imcValor,
            percentualGordura = gordura
        )
    }

    private fun mostrarErro(e: ApiException) {
        sucesso = false
        mensagem = e.mensagem
    }
}
