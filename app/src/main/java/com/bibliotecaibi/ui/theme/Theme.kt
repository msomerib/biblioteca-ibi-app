package com.bibliotecaibi.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun BibliotecaIBITheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = BluePrimary,
            secondary = BlueSecondary,
            tertiary = BlueDark
        ),
        typography = Typography(),
        content = content
    )
}
