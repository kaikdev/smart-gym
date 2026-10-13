package edu.projeto.smartgymfront.ui.componentes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListaRegistros(titulo: String, linhas: List<String>) {
    HorizontalDivider()
    Text(titulo, style = MaterialTheme.typography.titleMedium)
    if (linhas.isEmpty()) Text("Nenhum registro encontrado.")
    linhas.forEach { linha ->
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(linha)
            }
        }
    }
}
