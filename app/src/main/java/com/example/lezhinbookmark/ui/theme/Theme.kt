package com.example.lezhinbookmark.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = DarkBlue60,
    secondary = DarkBlue40,
    tertiary = DarkBlue30,
    surface = DarkBlue10,
    background = DarkWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = Blue60,
    secondary = Blue40,
    tertiary = Blue30,
    surface = Blue10,
    background = White
)

@Composable
fun LezhinBookmarkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}