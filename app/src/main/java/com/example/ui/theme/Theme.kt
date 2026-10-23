package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AgriGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = AgriNavSelected,
    onPrimaryContainer = Color.White,
    secondary = AgriDarkGreen,
    onSecondary = Color.White,
    tertiary = AgriGreenAccent,
    background = AgriBackground,
    onBackground = AgriTextPrimary,
    surface = AgriSurface,
    onSurface = AgriTextPrimary,
    surfaceVariant = AgriSurfaceVariant,
    onSurfaceVariant = AgriTextSecondary,
    outline = AgriBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
