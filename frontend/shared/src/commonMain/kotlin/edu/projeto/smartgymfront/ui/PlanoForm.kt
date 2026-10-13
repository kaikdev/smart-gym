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
import edu.projeto.smartgymfront.model.Plano
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.ListaRegistros
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.textoParaDouble
import edu.projeto.smartgymfront.ui.componentes.textoParaInt
import kotlinx.coroutines.launch

@Composable
fun PlanoForm(aoVoltar: () -> Unit) {
    var descricao by remember { mutableStateOf("") }
    var valorMensal by remember { mutableStateOf("") }
    var mesesFidelidade by remember { mutableStateOf("0") }
    var taxaMatricula by remember { mutableStateOf("0") }
    var promocional by remember { mutableStateOf(false) }

    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var planos by remember { mutableStateOf(listOf<Plano>()) }
    val escopo = rememberCoroutineScope()

    suspend fun carregar() {
        try {
            planos = ApiClient.listar("api/planos")
        } catch (e: ApiException) {
            sucesso = false
            mensagem = e.mensagem
        }
    }

    LaunchedEffect(Unit) { carregar() }

    fun limpar() {
        descricao = ""
        valorMensal = ""
        mesesFidelidade = "0"
        taxaMatricula = "0"
        promocional = false
        erros = emptyMap()
        mensagem = null
    }

    fun validar(): Plano? {
        val novos = mutableMapOf<String, String>()
        if (descricao.isBlank()) novos["descricao"] = "A descrição é obrigatória"
        else if (descricao.length > 100) novos["descricao"] = "A descrição deve ter no máximo 100 caracteres"

        val valor = textoParaDouble(valorMensal)
        if (valorMensal.isBlank()) novos["valorMensal"] = "O valor mensal é obrigatório"
        else if (valor == null) novos["valorMensal"] = "Valor inválido"
        else if (valor <= 0.0) novos["valorMensal"] = "O valor mensal deve ser maior que zero"

        val meses = textoParaInt(mesesFidelidade.ifBlank { "0" })
        if (meses == null) novos["mesesFidelidade"] = "Informe um número inteiro"
        else if (meses < 0) novos["mesesFidelidade"] = "Os meses de fidelidade não podem ser negativos"

        val taxa = textoParaDouble(taxaMatricula.ifBlank { "0" })
        if (taxa == null) novos["taxaMatricula"] = "Valor inválido"
        else if (taxa < 0.0) novos["taxaMatricula"] = "A taxa de matrícula não pode ser negativa"

        erros = novos
        if (novos.isNotEmpty() || valor == null || meses == null || taxa == null) return null
        return Plano(
            descricao = descricao.trim(),
            valorMensal = valor,
            mesesFidelidade = meses,
            taxaMatricula = taxa,
            promocional = promocional
        )
    }

    fun gravar() {
        mensagem = null
        val plano = validar() ?: return
        carregando = true
        escopo.launch {
            try {
                val gravado: Plano = ApiClient.criar("api/planos", plano)
                limpar()
                sucesso = true
                mensagem = "Plano gravado com sucesso (id ${gravado.id})"
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

    Tela("Cadastro de Plano", aoVoltar) {
        CampoTexto("Descrição", descricao, { descricao = it }, erros["descricao"])
        CampoTexto("Valor mensal (R$)", valorMensal, { valorMensal = it }, erros["valorMensal"], teclado = KeyboardType.Decimal)
        CampoTexto("Meses de fidelidade", mesesFidelidade, { mesesFidelidade = it }, erros["mesesFidelidade"], teclado = KeyboardType.Number)
        CampoTexto("Taxa de matrícula (R$)", taxaMatricula, { taxaMatricula = it }, erros["taxaMatricula"], teclado = KeyboardType.Decimal)
        CampoCheckbox("Promocional", promocional) { promocional = it }
        Mensagem(mensagem, sucesso)
        BotoesFormulario(aoGravar = ::gravar, aoLimpar = ::limpar, carregando = carregando)

        ListaRegistros(
            "Planos cadastrados",
            planos.map {
                "#${it.id} - ${it.descricao}\n" +
                    "Mensal: R$ ${it.valorMensal} | Fidelidade: ${it.mesesFidelidade} meses | " +
                    "Matrícula: R$ ${it.taxaMatricula}${if (it.promocional) " | Promocional" else ""}"
            }
        )
    }
}
