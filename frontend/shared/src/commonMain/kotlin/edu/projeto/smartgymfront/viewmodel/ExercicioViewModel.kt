package edu.projeto.smartgymfront.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Exercicio
import edu.projeto.smartgymfront.repositorio.ExercicioRepositorioRemoto
import edu.projeto.smartgymfront.ui.componentes.textoParaFloat
import edu.projeto.smartgymfront.ui.componentes.textoParaInt
import kotlinx.coroutines.launch

class ExercicioViewModel(private val repositorio: ExercicioRepositorioRemoto) : ViewModel() {

    var nome by mutableStateOf("")
    var grupoMuscular by mutableStateOf("")
    var series by mutableStateOf("")
    var repeticoes by mutableStateOf("")
    var cargaSugerida by mutableStateOf("")
    var precisaAcompanhamento by mutableStateOf(false)

    var lista by mutableStateOf(listOf<Exercicio>())
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
        grupoMuscular = ""
        series = ""
        repeticoes = ""
        cargaSugerida = ""
        precisaAcompanhamento = false
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
                grupoMuscular = registro.grupoMuscular
                series = registro.series.toString()
                repeticoes = registro.repeticoes.toString()
                cargaSugerida = registro.cargaSugerida?.toString() ?: ""
                precisaAcompanhamento = registro.precisaAcompanhamento
                idEdicao = id
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    fun gravar(aoConcluir: () -> Unit) {
        mensagem = null
        val exercicio = validar() ?: return
        carregando = true
        viewModelScope.launch {
            try {
                val id = idEdicao
                val gravado = if (id == null) repositorio.criar(exercicio) else repositorio.atualizar(id, exercicio)
                limpar()
                sucesso = true
                mensagem = if (id == null) "Exercício gravado com sucesso (id ${gravado.id})"
                else "Exercício atualizado com sucesso (id ${gravado.id})"
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
                mensagem = "Exercício apagado com sucesso (id $id)"
                carregar()
            } catch (e: ApiException) {
                mostrarErro(e)
            }
        }
    }

    private fun validar(): Exercicio? {
        val novos = mutableMapOf<String, String>()
        if (nome.isBlank()) novos["nome"] = "O nome é obrigatório"
        else if (nome.length > 100) novos["nome"] = "O nome deve ter no máximo 100 caracteres"

        if (grupoMuscular.isBlank()) novos["grupoMuscular"] = "O grupo muscular é obrigatório"
        else if (grupoMuscular.length > 50) novos["grupoMuscular"] = "O grupo muscular deve ter no máximo 50 caracteres"

        val seriesValor = textoParaInt(series)
        if (series.isBlank()) novos["series"] = "O número de séries é obrigatório"
        else if (seriesValor == null) novos["series"] = "Informe um número inteiro"
        else if (seriesValor < 1) novos["series"] = "O exercício deve ter no mínimo 1 série"

        val repeticoesValor = textoParaInt(repeticoes)
        if (repeticoes.isBlank()) novos["repeticoes"] = "O número de repetições é obrigatório"
        else if (repeticoesValor == null) novos["repeticoes"] = "Informe um número inteiro"
        else if (repeticoesValor < 1) novos["repeticoes"] = "O exercício deve ter no mínimo 1 repetição"

        val carga = if (cargaSugerida.isBlank()) null else textoParaFloat(cargaSugerida)
        if (cargaSugerida.isNotBlank() && carga == null) novos["cargaSugerida"] = "Carga inválida"
        else if (carga != null && carga < 0f) novos["cargaSugerida"] = "A carga sugerida não pode ser negativa"

        erros = novos
        if (novos.isNotEmpty() || seriesValor == null || repeticoesValor == null) return null
        return Exercicio(
            nome = nome.trim(),
            grupoMuscular = grupoMuscular.trim(),
            series = seriesValor,
            repeticoes = repeticoesValor,
            cargaSugerida = carga,
            precisaAcompanhamento = precisaAcompanhamento
        )
    }

    private fun mostrarErro(e: ApiException) {
        sucesso = false
        mensagem = e.mensagem
    }
}
