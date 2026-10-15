package edu.projeto.smartgymfront.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import edu.projeto.smartgymfront.api.ApiClient
import edu.projeto.smartgymfront.api.ApiException
import edu.projeto.smartgymfront.model.Exercicio
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.ListaRegistros
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.textoParaFloat
import edu.projeto.smartgymfront.ui.componentes.textoParaInt
import kotlinx.coroutines.launch

@Composable
fun ExercicioForm(aoVoltar: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var grupoMuscular by remember { mutableStateOf("") }
    var series by remember { mutableStateOf("") }
    var repeticoes by remember { mutableStateOf("") }
    var cargaSugerida by remember { mutableStateOf("") }
    var precisaAcompanhamento by remember { mutableStateOf(false) }

    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var exercicios by remember { mutableStateOf(listOf<Exercicio>()) }
    val escopo = rememberCoroutineScope()

    suspend fun carregar() {
        try {
            exercicios = ApiClient.listar("api/exercicios")
        } catch (e: ApiException) {
            sucesso = false
            mensagem = e.mensagem
        }
    }

    LaunchedEffect(Unit) { carregar() }

    fun limpar() {
        nome = ""
        grupoMuscular = ""
        series = ""
        repeticoes = ""
        cargaSugerida = ""
        precisaAcompanhamento = false
        erros = emptyMap()
        mensagem = null
    }

    fun validar(): Exercicio? {
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

    fun gravar() {
        mensagem = null
        val exercicio = validar() ?: return
        carregando = true
        escopo.launch {
            try {
                val gravado: Exercicio = ApiClient.criar("api/exercicios", exercicio)
                limpar()
                sucesso = true
                mensagem = "Exercício gravado com sucesso (id ${gravado.id})"
                carregar()
            } catch (e: ApiException) {
                erros = e.erros
                sucesso = false
                mensagem = e.mensagem
            } finally {
                carregando = false
            }
        }
    }

    Tela("Cadastro de Exercício", aoVoltar) {
        CampoTexto("Nome", nome, { nome = it }, erros["nome"])
        CampoTexto("Grupo muscular", grupoMuscular, { grupoMuscular = it }, erros["grupoMuscular"], dica = "ex.: Peito, Costas, Pernas")
        CampoTexto("Séries", series, { series = it }, erros["series"], teclado = KeyboardType.Number)
        CampoTexto("Repetições", repeticoes, { repeticoes = it }, erros["repeticoes"], teclado = KeyboardType.Number)
        CampoTexto("Carga sugerida (kg)", cargaSugerida, { cargaSugerida = it }, erros["cargaSugerida"], teclado = KeyboardType.Decimal)
        CampoCheckbox("Precisa de acompanhamento", precisaAcompanhamento) { precisaAcompanhamento = it }
        Mensagem(mensagem, sucesso)
        BotoesFormulario(aoGravar = ::gravar, aoLimpar = ::limpar, carregando = carregando)

        ListaRegistros(
            "Exercícios cadastrados",
            exercicios.map {
                "#${it.id} - ${it.nome} (${it.grupoMuscular})\n" +
                    "${it.series} x ${it.repeticoes} | Carga: ${it.cargaSugerida?.let { c -> "$c kg" } ?: "-"}" +
                    if (it.precisaAcompanhamento) " | Precisa de acompanhamento" else ""
            }
        )
    }
}
