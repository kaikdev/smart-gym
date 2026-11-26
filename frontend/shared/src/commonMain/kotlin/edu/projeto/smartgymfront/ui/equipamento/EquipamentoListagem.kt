package edu.projeto.smartgymfront.ui.equipamento

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.projeto.smartgymfront.sessao.SessaoUsuario
import edu.projeto.smartgymfront.ui.componentes.CardRegistro
import edu.projeto.smartgymfront.ui.componentes.Mensagem
import edu.projeto.smartgymfront.ui.componentes.Tela
import edu.projeto.smartgymfront.ui.componentes.dataParaTexto
import edu.projeto.smartgymfront.viewmodel.EquipamentoViewModel

@Composable
fun EquipamentoListagem(
    viewModel: EquipamentoViewModel,
    aoNovo: () -> Unit,
    aoEditar: (Long) -> Unit,
    aoVoltar: () -> Unit
) {
    Tela("Equipamentos", aoVoltar) {
        if (SessaoUsuario.admin) {
            Button(onClick = aoNovo, modifier = Modifier.fillMaxWidth()) {
                Text("Novo equipamento")
            }
        }
        OutlinedButton(onClick = viewModel::carregar, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Filled.Refresh, contentDescription = null)
            Text("Atualizar status dos equipamentos", modifier = Modifier.padding(start = 8.dp))
        }
        Mensagem(viewModel.mensagem, viewModel.sucesso)
        if (viewModel.lista.isEmpty()) Text("Nenhum registro encontrado.")
        viewModel.lista.forEach { item ->
            val id = item.id ?: return@forEach
            CardRegistro(
                titulo = "#${item.id} - ${item.nome}",
                campos = listOf(
                    "Fabricante: ${item.fabricante ?: "-"}",
                    "Data de compra: ${dataParaTexto(item.dataCompra).ifBlank { "-" }}",
                    "Peso suportado: ${item.pesoSuportado} kg",
                    "Sensor: ${item.codigoSensorIot ?: "-"} | ${if (item.emUsoIot) "Em uso" else "Livre"}"
                ),
                aoEditar = { aoEditar(id) },
                aoApagar = { viewModel.apagar(id) }
            )
        }
    }
}
