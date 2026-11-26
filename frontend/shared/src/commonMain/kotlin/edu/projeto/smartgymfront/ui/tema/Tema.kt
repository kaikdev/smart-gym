package edu.projeto.smartgymfront.ui.tema

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CoresSmartGym = darkColorScheme(
    primary = Color(0xFFC6F432),
    onPrimary = Color(0xFF14171C),
    primaryContainer = Color(0xFF3A4A12),
    onPrimaryContainer = Color(0xFFE4FF9A),
    secondary = Color(0xFF2DD4BF),
    onSecondary = Color(0xFF062A25),
    background = Color(0xFF0F1115),
    onBackground = Color(0xFFE8EAED),
    surface = Color(0xFF0F1115),
    onSurface = Color(0xFFE8EAED),
    surfaceVariant = Color(0xFF1E2229),
    onSurfaceVariant = Color(0xFFB4BAC4),
    surfaceContainerLowest = Color(0xFF0B0D10),
    surfaceContainerLow = Color(0xFF161A1F),
    surfaceContainer = Color(0xFF1A1E24),
    surfaceContainerHigh = Color(0xFF1E2229),
    surfaceContainerHighest = Color(0xFF252A32),
    outline = Color(0xFF3A414C),
    outlineVariant = Color(0xFF2A3038),
    error = Color(0xFFFF6B6B),
    onError = Color(0xFF2B0A0A)
)

@Composable
fun TemaSmartGym(conteudo: @Composable () -> Unit) {
    MaterialTheme(colorScheme = CoresSmartGym, content = conteudo)
}
