package edu.projeto.smartgymfront.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Treino
import edu.projeto.smartgymfront.repositorio.TreinoRepositorioRemoto
import edu.projeto.smartgymfront.ui.componentes.textoParaInt
import kotlinx.coroutines.launch

class TreinoViewModel(private val repositorio: TreinoRepositorioRemoto) : ViewModel() {

    var titulo by mutableStateOf("")
    var objetivo by mutableStateOf("")
    var duracaoEstimadaMin by mutableStateOf("")
    var publico by mutableStateOf(false)

    var lista by mutableStateOf(listOf<Treino>())
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
        titulo = ""
        objetivo = ""
        duracaoEstimadaMin = ""
        publico = false
        idEdicao = null
        erros = emptyMap()
        mensagem = null
    }

    fun editar(id: Long) {
        limpar()
        viewModelScope.launch {
            try {
                val registro = repositorio.buscarPorId(id)
                titulo = registro.titulo
                objetivo = registro.objetivo ?: ""
                duracaoEstimadaMin = registro.duracaoEstimadaMin.toString()
                publico = registro.publico
                idEdicao = id
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun gravar(aoConcluir: () -> Unit) {
        mensagem = null
        val treino = validar() ?: return
        carregando = true
        viewModelScope.launch {
            try {
                val id = idEdicao
                val gravado = if (id == null) repositorio.criar(treino) else repositorio.atualizar(id, treino)
                limpar()
                sucesso = true
                mensagem = if (id == null) "Treino gravado com sucesso (id ${gravado.id})"
                else "Treino atualizado com sucesso (id ${gravado.id})"
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
                mensagem = "Treino apagado com sucesso (id $id)"
                carregar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    private fun validar(): Treino? {
        val novos = mutableMapOf<String, String>()
        if (titulo.isBlank()) novos["titulo"] = "O título é obrigatório"
        else if (titulo.length > 80) novos["titulo"] = "O título deve ter no máximo 80 caracteres"

        if (objetivo.length > 100) novos["objetivo"] = "O objetivo deve ter no máximo 100 caracteres"

        val duracao = textoParaInt(duracaoEstimadaMin)
        if (duracaoEstimadaMin.isBlank()) novos["duracaoEstimadaMin"] = "A duração estimada é obrigatória"
        else if (duracao == null) novos["duracaoEstimadaMin"] = "Informe um número inteiro"
        else if (duracao < 1) novos["duracaoEstimadaMin"] = "A duração estimada deve ser de no mínimo 1 minuto"

        erros = novos
        if (novos.isNotEmpty() || duracao == null) return null
        return Treino(
            titulo = titulo.trim(),
            objetivo = objetivo.trim().ifBlank { null },
            duracaoEstimadaMin = duracao,
            publico = publico
        )
    }

    private fun mostrarErro(e: ApiException) {
        sucesso = false
        mensagem = e.mensagem
    }
}
