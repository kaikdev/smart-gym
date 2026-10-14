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
import edu.projeto.smartgymfront.model.Agendamento
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.ListaRegistros
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.agora
import edu.projeto.smartgymfront.ui.componentes.dataHoraParaTexto
import edu.projeto.smartgymfront.ui.componentes.textoParaDataHora
import edu.projeto.smartgymfront.ui.componentes.textoParaInt
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

@Composable
fun AgendamentoForm(aoVoltar: () -> Unit) {
    var tipoAula by remember { mutableStateOf("") }
    var instrutor by remember { mutableStateOf("") }
    var dataHoraAgendada by remember { mutableStateOf("") }
    var capacidadeMaxima by remember { mutableStateOf("") }
    var cancelado by remember { mutableStateOf(false) }

    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var agendamentos by remember { mutableStateOf(listOf<Agendamento>()) }
    val escopo = rememberCoroutineScope()

    suspend fun carregar() {
        try {
            agendamentos = ApiClient.listar("api/agendamentos")
        } catch (e: ApiException) {
            sucesso = false
            mensagem = e.mensagem
        }
    }

    LaunchedEffect(Unit) { carregar() }

    fun limpar() {
        tipoAula = ""
        instrutor = ""
        dataHoraAgendada = ""
        capacidadeMaxima = ""
        cancelado = false
        erros = emptyMap()
        mensagem = null
    }

    fun validar(): Agendamento? {
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

    fun gravar() {
        mensagem = null
        val agendamento = validar() ?: return
        carregando = true
        escopo.launch {
            try {
                val gravado: Agendamento = ApiClient.criar("api/agendamentos", agendamento)
                limpar()
                sucesso = true
                mensagem = "Agendamento gravado com sucesso (id ${gravado.id})"
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

    Tela("Cadastro de Agendamento", aoVoltar) {
        CampoTexto("Tipo de aula", tipoAula, { tipoAula = it }, erros["tipoAula"])
        CampoTexto("Instrutor", instrutor, { instrutor = it }, erros["instrutor"])
        CampoTexto(
            "Data e hora",
            dataHoraAgendada,
            { dataHoraAgendada = it },
            erros["dataHoraAgendada"],
            dica = "dd/mm/aaaa hh:mm"
        )
        CampoTexto(
            "Capacidade máxima",
            capacidadeMaxima,
            { capacidadeMaxima = it },
            erros["capacidadeMaxima"],
            teclado = KeyboardType.Number
        )
        CampoCheckbox("Cancelado", cancelado) { cancelado = it }
        Mensagem(mensagem, sucesso)
        BotoesFormulario(aoGravar = ::gravar, aoLimpar = ::limpar, carregando = carregando)

        ListaRegistros(
            "Agendamentos cadastrados",
            agendamentos.map {
                "#${it.id} - ${it.tipoAula} em ${dataHoraParaTexto(it.dataHoraAgendada)}\n" +
                    "Instrutor: ${it.instrutor ?: "-"} | Capacidade: ${it.capacidadeMaxima} | " +
                    if (it.cancelado) "Cancelado" else "Ativo"
            }
        )
    }
}
