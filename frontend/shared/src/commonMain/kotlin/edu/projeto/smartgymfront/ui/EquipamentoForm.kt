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
import edu.projeto.smartgymfront.model.Equipamento
import edu.projeto.smartgymfront.ui.componentes.BotoesFormulario
import edu.projeto.smartgymfront.ui.componentes.CampoCheckbox
import edu.projeto.smartgymfront.ui.componentes.CampoTexto
import edu.projeto.smartgymfront.ui.componentes.ListaRegistros
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.ui.componentes.hoje
import edu.projeto.smartgymfront.ui.componentes.textoParaData
import edu.projeto.smartgymfront.ui.componentes.textoParaDouble
import kotlinx.coroutines.launch

@Composable
fun EquipamentoForm(aoVoltar: () -> Unit) {
    var nome by remember { mutableStateOf("") }
    var fabricante by remember { mutableStateOf("") }
    var dataCompra by remember { mutableStateOf("") }
    var pesoSuportado by remember { mutableStateOf("") }
    var codigoSensorIot by remember { mutableStateOf("") }
    var emUsoIot by remember { mutableStateOf(false) }

    var erros by remember { mutableStateOf(mapOf<String, String>()) }
    var mensagem by remember { mutableStateOf<String?>(null) }
    var sucesso by remember { mutableStateOf(false) }
    var carregando by remember { mutableStateOf(false) }
    var equipamentos by remember { mutableStateOf(listOf<Equipamento>()) }
    val escopo = rememberCoroutineScope()

    suspend fun carregar() {
        try {
            equipamentos = ApiClient.listar("api/equipamentos")
        } catch (e: ApiException) {
            sucesso = false
            mensagem = e.mensagem
        }
    }

    LaunchedEffect(Unit) { carregar() }

    fun limpar() {
        nome = ""
        fabricante = ""
        dataCompra = ""
        pesoSuportado = ""
        codigoSensorIot = ""
        emUsoIot = false
        erros = emptyMap()
        mensagem = null
    }

    fun validar(): Equipamento? {
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

    fun gravar() {
        mensagem = null
        val equipamento = validar() ?: return
        carregando = true
        escopo.launch {
            try {
                val gravado: Equipamento = ApiClient.criar("api/equipamentos", equipamento)
                limpar()
                sucesso = true
                mensagem = "Equipamento gravado com sucesso (id ${gravado.id})"
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

    Tela("Cadastro de Equipamento", aoVoltar) {
        CampoTexto("Nome", nome, { nome = it }, erros["nome"])
        CampoTexto("Fabricante", fabricante, { fabricante = it }, erros["fabricante"])
        CampoTexto(
            "Data de compra",
            dataCompra,
            { dataCompra = it },
            erros["dataCompra"],
            teclado = KeyboardType.Number,
            dica = "dd/mm/aaaa"
        )
        CampoTexto("Peso suportado (kg)", pesoSuportado, { pesoSuportado = it }, erros["pesoSuportado"], teclado = KeyboardType.Decimal)
        CampoTexto("Código do sensor IoT", codigoSensorIot, { codigoSensorIot = it }, erros["codigoSensorIot"])
        CampoCheckbox("Em uso (sensor IoT)", emUsoIot) { emUsoIot = it }
        Mensagem(mensagem, sucesso)
        BotoesFormulario(aoGravar = ::gravar, aoLimpar = ::limpar, carregando = carregando)

        ListaRegistros(
            "Equipamentos cadastrados",
            equipamentos.map {
                "#${it.id} - ${it.nome} (${it.fabricante ?: "sem fabricante"})\n" +
                    "Compra: ${dataParaTexto(it.dataCompra).ifBlank { "-" }} | Suporta: ${it.pesoSuportado} kg | " +
                    "Sensor: ${it.codigoSensorIot ?: "-"} | " +
                    if (it.emUsoIot) "Em uso" else "Livre"
            }
        )
    }
}
