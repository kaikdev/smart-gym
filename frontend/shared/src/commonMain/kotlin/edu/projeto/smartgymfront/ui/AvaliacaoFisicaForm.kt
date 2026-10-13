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
import edu.projeto.smartgymfront.model.AvaliacaoFisica
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.ListaRegistros
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.ui.componentes.hoje
import edu.projeto.smartgymfront.ui.componentes.textoParaData
import edu.projeto.smartgymfront.ui.componentes.textoParaDouble
import edu.projeto.smartgymfront.ui.componentes.textoParaFloat
import kotlinx.coroutines.launch

@Composable
fun AvaliacaoFisicaForm(aoVoltar: () -> Unit) {
    var avaliador by remember { mutableStateOf("") }
    var dataAvaliacao by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var imc by remember { mutableStateOf("") }
    var percentualGordura by remember { mutableStateOf("") }

    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var avaliacoes by remember { mutableStateOf(listOf<AvaliacaoFisica>()) }
    val escopo = rememberCoroutineScope()

    suspend fun carregar() {
        try {
            avaliacoes = ApiClient.listar("api/avaliacoes")
        } catch (e: ApiException) {
            sucesso = false
            mensagem = e.mensagem
        }
    }

    LaunchedEffect(Unit) { carregar() }

    fun limpar() {
        avaliador = ""
        dataAvaliacao = ""
        altura = ""
        imc = ""
        percentualGordura = ""
        erros = emptyMap()
        mensagem = null
    }

    fun validar(): AvaliacaoFisica? {
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

    fun gravar() {
        mensagem = null
        val avaliacao = validar() ?: return
        carregando = true
        escopo.launch {
            try {
                val gravada: AvaliacaoFisica = ApiClient.criar("api/avaliacoes", avaliacao)
                limpar()
                sucesso = true
                mensagem = "Avaliação gravada com sucesso (id ${gravada.id})"
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

    Tela("Cadastro de Avaliação Física", aoVoltar) {
        CampoTexto("Avaliador", avaliador, { avaliador = it }, erros["avaliador"])
        CampoTexto(
            "Data da avaliação",
            dataAvaliacao,
            { dataAvaliacao = it },
            erros["dataAvaliacao"],
            teclado = KeyboardType.Number,
            dica = "dd/mm/aaaa"
        )
        CampoTexto("Altura (m)", altura, { altura = it }, erros["altura"], teclado = KeyboardType.Decimal, dica = "ex.: 1,75")
        CampoTexto("IMC", imc, { imc = it }, erros["imc"], teclado = KeyboardType.Decimal)
        CampoTexto(
            "Percentual de gordura (%)",
            percentualGordura,
            { percentualGordura = it },
            erros["percentualGordura"],
            teclado = KeyboardType.Decimal
        )
        Mensagem(mensagem, sucesso)
        BotoesFormulario(aoGravar = ::gravar, aoLimpar = ::limpar, carregando = carregando)

        ListaRegistros(
            "Avaliações cadastradas",
            avaliacoes.map {
                "#${it.id} - ${it.avaliador} em ${dataParaTexto(it.dataAvaliacao)}\n" +
                    "Altura: ${it.altura} m | IMC: ${it.imc} | Gordura: ${it.percentualGordura?.let { g -> "$g%" } ?: "-"}"
            }
        )
    }
}
