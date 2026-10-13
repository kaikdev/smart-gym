package edu.projeto.smartgymfront.ui.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun CampoTexto(
    rotulo: String,
    valor: String,
    aoMudar: (String) -> Unit,
    erro: String? = null,
    senha: Boolean = false,
    teclado: KeyboardType = KeyboardType.Text,
    dica: String? = null
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(rotulo) },
        placeholder = if (dica != null) { { Text(dica) } } else null,
        isError = erro != null,
        supportingText = if (erro != null) { { Text(erro) } } else null,
        singleLine = true,
        visualTransformation = if (senha) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (senha) KeyboardType.Password else teclado),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CampoCheckbox(rotulo: String, marcado: Boolean, aoMudar: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = marcado, onCheckedChange = aoMudar)
        Text(rotulo)
    }
}

@Composable
fun BotoesFormulario(aoGravar: () -> Unit, aoLimpar: () -> Unit, carregando: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Button(onClick = aoGravar, enabled = !carregando, modifier = Modifier.weight(1f)) {
            Text("Gravar")
        }
        OutlinedButton(onClick = aoLimpar, enabled = !carregando, modifier = Modifier.weight(1f)) {
            Text("Limpar Campos")
        }
    }
    if (carregando) CircularProgressIndicator()
}

@Composable
fun Mensagem(texto: String?, sucesso: Boolean) {
    if (texto == null) return
    Text(
        texto,
        color = if (sucesso) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    )
}

@Composable
fun Tela(titulo: String, aoVoltar: (() -> Unit)? = null, conteudo: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            if (aoVoltar != null) OutlinedButton(onClick = aoVoltar) { Text("Voltar") }
            Text(titulo, style = MaterialTheme.typography.headlineSmall)
        }
        conteudo()
    }
}
