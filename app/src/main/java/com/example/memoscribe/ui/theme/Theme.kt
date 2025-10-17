package com.example.memoscribe.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Secondary,
    tertiary = SecondaryVariant,
    background = Color(0xFF121212),
    surface = Color(0xFF1F1F1F),
    error = Error,
    onPrimary = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF)

    /*primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
     */
)

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = SecondaryVariant,
    background = Background,
    surface = Surface,
    error = Error,
    onPrimary = OnPrimary,
    onBackground = OnBackground,
    onSurface = OnSurface

    /*primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
    */

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onSecondary = Color.White,
    onTertiary = Color.White,
    */
)

@Composable
fun MemoScribeTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}