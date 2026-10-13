package edu.projeto.smartgymfront.ui

import androidx.compose.material3.Text
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
import edu.projeto.smartgymfront.model.Treino
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.ListaRegistros
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.ui.componentes.textoParaInt
import kotlinx.coroutines.launch

@Composable
fun TreinoForm(aoVoltar: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var objetivo by remember { mutableStateOf("") }
    var duracaoEstimadaMin by remember { mutableStateOf("") }
    var publico by remember { mutableStateOf(false) }

    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var treinos by remember { mutableStateOf(listOf<Treino>()) }
    val escopo = rememberCoroutineScope()

    suspend fun carregar() {
        try {
            treinos = ApiClient.listar("api/treinos")
        } catch (e: ApiException) {
            sucesso = false
            mensagem = e.mensagem
        }
    }

    LaunchedEffect(Unit) { carregar() }

    fun limpar() {
        titulo = ""
        objetivo = ""
        duracaoEstimadaMin = ""
        publico = false
        erros = emptyMap()
        mensagem = null
    }

    fun validar(): Treino? {
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

    fun gravar() {
        mensagem = null
        val treino = validar() ?: return
        carregando = true
        escopo.launch {
            try {
                val gravado: Treino = ApiClient.criar("api/treinos", treino)
                limpar()
                sucesso = true
                mensagem = "Treino gravado com sucesso (id ${gravado.id})"
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

    Tela("Cadastro de Treino", aoVoltar) {
        CampoTexto("Título", titulo, { titulo = it }, erros["titulo"])
        CampoTexto("Objetivo", objetivo, { objetivo = it }, erros["objetivo"])
        CampoTexto(
            "Duração estimada (min)",
            duracaoEstimadaMin,
            { duracaoEstimadaMin = it },
            erros["duracaoEstimadaMin"],
            teclado = KeyboardType.Number
        )
        CampoCheckbox("Público", publico) { publico = it }
        Text("A data de criação é preenchida automaticamente ao gravar.")
        Mensagem(mensagem, sucesso)
        BotoesFormulario(aoGravar = ::gravar, aoLimpar = ::limpar, carregando = carregando)

        ListaRegistros(
            "Treinos cadastrados",
            treinos.map {
                "#${it.id} - ${it.titulo}\n" +
                    "Objetivo: ${it.objetivo ?: "-"} | Duração: ${it.duracaoEstimadaMin} min | " +
                    "Criado em: ${dataParaTexto(it.dataCriacao).ifBlank { "-" }} | " +
                    if (it.publico) "Público" else "Privado"
            }
        )
    }
}
