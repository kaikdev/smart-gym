package edu.projeto.smartgymfront.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Agendamento
import edu.projeto.smartgymfront.repositorio.AgendamentoRepositorioRemoto
import edu.projeto.smartgymfront.ui.componentes.agora
import edu.projeto.smartgymfront.ui.componentes.dataHoraParaTexto
import edu.projeto.smartgymfront.ui.componentes.textoParaDataHora
import edu.projeto.smartgymfront.ui.componentes.textoParaInt
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

class AgendamentoViewModel(private val repositorio: AgendamentoRepositorioRemoto) : ViewModel() {

    var tipoAula by mutableStateOf("")
    var instrutor by mutableStateOf("")
    var dataHoraAgendada by mutableStateOf("")
    var capacidadeMaxima by mutableStateOf("")
    var cancelado by mutableStateOf(false)

    var lista by mutableStateOf(listOf<Agendamento>())
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
        tipoAula = ""
        instrutor = ""
        dataHoraAgendada = ""
        capacidadeMaxima = ""
        cancelado = false
        idEdicao = null
        erros = emptyMap()
        mensagem = null
    }

    fun editar(id: Long) {
        limpar()
        viewModelScope.launch {
            try {
                val registro = repositorio.buscarPorId(id)
                tipoAula = registro.tipoAula
                instrutor = registro.instrutor ?: ""
                dataHoraAgendada = dataHoraParaTexto(registro.dataHoraAgendada)
                capacidadeMaxima = registro.capacidadeMaxima.toString()
                cancelado = registro.cancelado
                idEdicao = id
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun gravar(aoConcluir: () -> Unit) {
        mensagem = null
        val agendamento = validar() ?: return
        carregando = true
        viewModelScope.launch {
            try {
                val id = idEdicao
                val gravado = if (id == null) repositorio.criar(agendamento) else repositorio.atualizar(id, agendamento)
                limpar()
                sucesso = true
                mensagem = if (id == null) "Agendamento gravado com sucesso (id ${gravado.id})"
                else "Agendamento atualizado com sucesso (id ${gravado.id})"
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
                mensagem = "Agendamento apagado com sucesso (id $id)"
                carregar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    private fun validar(): Agendamento? {
        val novos = mutableMapOf<String, String>()
        if (tipoAula.isBlank()) novos["tipoAula"] = "O tipo de aula é obrigatório"
        else if (tipoAula.length > 60) novos["tipoAula"] = "O tipo de aula deve ter no máximo 60 caracteres"

        if (instrutor.length > 100) novos["instrutor"] = "O nome do instrutor deve ter no máximo 100 caracteres"

        val dataHora = textoParaDataHora(dataHoraAgendada)
        val minutoAtual = agora().let { LocalDateTime(it.date, LocalTime(it.hour, it.minute)) }
        if (dataHoraAgendada.isBlank()) novos["dataHoraAgendada"] = "A data e hora do agendamento são obrigatórias"
        else if (dataHora == null) novos["dataHoraAgendada"] = "Data e hora inválidas, use dd/mm/aaaa hh:mm"
        else if (dataHora < minutoAtual) novos["dataHoraAgendada"] = "O agendamento não pode ser marcado no passado"

        val capacidade = textoParaInt(capacidadeMaxima)
        if (capacidadeMaxima.isBlank()) novos["capacidadeMaxima"] = "A capacidade máxima é obrigatória"
        else if (capacidade == null) novos["capacidadeMaxima"] = "Informe um número inteiro"
        else if (capacidade < 1) novos["capacidadeMaxima"] = "A capacidade máxima deve ser de no mínimo 1 pessoa"

        erros = novos
        if (novos.isNotEmpty() || dataHora == null || capacidade == null) return null
        return Agendamento(
            tipoAula = tipoAula.trim(),
            instrutor = instrutor.trim().ifBlank { null },
            dataHoraAgendada = dataHora,
            capacidadeMaxima = capacidade,
            cancelado = cancelado
        )
    }

    private fun mostrarErro(e: ApiException) {
        sucesso = false
        mensagem = e.mensagem
    }
}
