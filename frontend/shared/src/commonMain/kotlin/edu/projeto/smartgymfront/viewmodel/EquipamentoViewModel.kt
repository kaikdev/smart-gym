package edu.projeto.smartgymfront.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Equipamento
import edu.projeto.smartgymfront.repositorio.EquipamentoRepositorioRemoto
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.ui.componentes.hoje
import edu.projeto.smartgymfront.ui.componentes.textoParaData
import edu.projeto.smartgymfront.ui.componentes.textoParaDouble
import kotlinx.coroutines.launch

class EquipamentoViewModel(private val repositorio: EquipamentoRepositorioRemoto) : ViewModel() {

    var nome by mutableStateOf("")
    var fabricante by mutableStateOf("")
    var dataCompra by mutableStateOf("")
    var pesoSuportado by mutableStateOf("")
    var codigoSensorIot by mutableStateOf("")
    var emUsoIot by mutableStateOf(false)

    var lista by mutableStateOf(listOf<Equipamento>())
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
        nome = ""
        fabricante = ""
        dataCompra = ""
        pesoSuportado = ""
        codigoSensorIot = ""
        emUsoIot = false
        idEdicao = null
        erros = emptyMap()
        mensagem = null
    }

    fun editar(id: Long) {
        limpar()
        viewModelScope.launch {
            try {
                val registro = repositorio.buscarPorId(id)
                nome = registro.nome
                fabricante = registro.fabricante ?: ""
                dataCompra = dataParaTexto(registro.dataCompra)
                pesoSuportado = registro.pesoSuportado.toString()
                codigoSensorIot = registro.codigoSensorIot ?: ""
                emUsoIot = registro.emUsoIot
                idEdicao = id
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun gravar(aoConcluir: () -> Unit) {
        mensagem = null
        val equipamento = validar() ?: return
        carregando = true
        viewModelScope.launch {
            try {
                val id = idEdicao
                val gravado = if (id == null) repositorio.criar(equipamento) else repositorio.atualizar(id, equipamento)
                limpar()
                sucesso = true
                mensagem = if (id == null) "Equipamento gravado com sucesso (id ${gravado.id})"
                else "Equipamento atualizado com sucesso (id ${gravado.id})"
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
                mensagem = "Equipamento apagado com sucesso (id $id)"
                carregar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    private fun validar(): Equipamento? {
        val novos = mutableMapOf<String, String>()
        if (nome.isBlank()) novos["nome"] = "O nome é obrigatório"
        else if (nome.length > 100) novos["nome"] = "O nome deve ter no máximo 100 caracteres"

        if (fabricante.length > 50) novos["fabricante"] = "O fabricante deve ter no máximo 50 caracteres"

        val data = if (dataCompra.isBlank()) null else textoParaData(dataCompra)
        if (dataCompra.isNotBlank() && data == null) novos["dataCompra"] = "Data inválida, use dd/mm/aaaa"
        else if (data != null && data > hoje()) novos["dataCompra"] = "A data de compra não pode estar no futuro"

        val peso = textoParaDouble(pesoSuportado)
        if (pesoSuportado.isBlank()) novos["pesoSuportado"] = "O peso suportado é obrigatório"
        else if (peso == null) novos["pesoSuportado"] = "Peso inválido"
        else if (peso < 1.0) novos["pesoSuportado"] = "O peso suportado deve ser no mínimo 1"

        if (codigoSensorIot.length > 50) novos["codigoSensorIot"] = "O código do sensor deve ter no máximo 50 caracteres"

        erros = novos
        if (novos.isNotEmpty() || peso == null) return null
        return Equipamento(
            nome = nome.trim(),
            fabricante = fabricante.trim().ifBlank { null },
            dataCompra = data,
            pesoSuportado = peso,
            codigoSensorIot = codigoSensorIot.trim().ifBlank { null },
            emUsoIot = emUsoIot
        )
    }

    private fun mostrarErro(e: ApiException) {
        sucesso = false
        mensagem = e.mensagem
    }
}
