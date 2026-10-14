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
import edu.projeto.smartgymfront.model.Pagamento
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.ListaRegistros
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataHoraParaTexto
import edu.projeto.smartgymfront.ui.componentes.textoParaDouble
import kotlinx.coroutines.launch

@Composable
fun PagamentoForm(aoVoltar: () -> Unit) {
    var metodo by remember { mutableStateOf("") }
    var codigoTransacao by remember { mutableStateOf("") }
    var valorFinal by remember { mutableStateOf("") }
    var confirmado by remember { mutableStateOf(false) }

    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var pagamentos by remember { mutableStateOf(listOf<Pagamento>()) }
    val escopo = rememberCoroutineScope()

    suspend fun carregar() {
        try {
            pagamentos = ApiClient.listar("api/pagamentos")
        } catch (e: ApiException) {
            sucesso = false
            mensagem = e.mensagem
        }
    }

    LaunchedEffect(Unit) { carregar() }

    fun limpar() {
        metodo = ""
        codigoTransacao = ""
        valorFinal = ""
        confirmado = false
        erros = emptyMap()
        mensagem = null
    }

    fun validar(): Pagamento? {
        val novos = mutableMapOf<String, String>()
        if (metodo.isBlank()) novos["metodo"] = "O método de pagamento é obrigatório"
        else if (metodo.length > 40) novos["metodo"] = "O método deve ter no máximo 40 caracteres"

        if (codigoTransacao.length > 100) novos["codigoTransacao"] = "O código da transação deve ter no máximo 100 caracteres"

        val valor = textoParaDouble(valorFinal)
        if (valorFinal.isBlank()) novos["valorFinal"] = "O valor final é obrigatório"
        else if (valor == null) novos["valorFinal"] = "Valor inválido"
        else if (valor <= 0.0) novos["valorFinal"] = "O valor final deve ser maior que zero"

        erros = novos
        if (novos.isNotEmpty() || valor == null) return null
        return Pagamento(
            metodo = metodo.trim(),
            codigoTransacao = codigoTransacao.trim().ifBlank { null },
            valorFinal = valor,
            confirmado = confirmado
        )
    }

    fun gravar() {
        mensagem = null
        val pagamento = validar() ?: return
        carregando = true
        escopo.launch {
            try {
                val gravado: Pagamento = ApiClient.criar("api/pagamentos", pagamento)
                limpar()
                sucesso = true
                mensagem = "Pagamento gravado com sucesso (id ${gravado.id})"
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

    Tela("Cadastro de Pagamento", aoVoltar) {
        CampoTexto("Método", metodo, { metodo = it }, erros["metodo"], dica = "PIX, Cartão de crédito, Boleto, Dinheiro")
        CampoTexto("Código da transação", codigoTransacao, { codigoTransacao = it }, erros["codigoTransacao"])
        CampoTexto("Valor final (R$)", valorFinal, { valorFinal = it }, erros["valorFinal"], teclado = KeyboardType.Decimal)
        CampoCheckbox("Confirmado", confirmado) { confirmado = it }
        Text("A data e hora de processamento são preenchidas automaticamente ao gravar.")
        Mensagem(mensagem, sucesso)
        BotoesFormulario(aoGravar = ::gravar, aoLimpar = ::limpar, carregando = carregando)

        ListaRegistros(
            "Pagamentos cadastrados",
            pagamentos.map {
                "#${it.id} - ${it.metodo} | R$ ${it.valorFinal}\n" +
                    "Código: ${it.codigoTransacao ?: "-"} | Processado em: ${dataHoraParaTexto(it.dataProcessamento)} | " +
                    if (it.confirmado) "Confirmado" else "Pendente"
            }
        )
    }
}
