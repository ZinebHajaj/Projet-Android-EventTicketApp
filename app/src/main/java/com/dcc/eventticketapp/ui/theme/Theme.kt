package com.dcc.eventticketapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val LightColorScheme = lightColorScheme(
    primary             = OrangeMain,
    onPrimary           = Color.White,
    secondary           = OrangeDark,
    onSecondary         = Color.White,
    tertiary            = OrangeLight,
    onTertiary          = TextDarkMode,
    background          = BgLightMode,
    onBackground        = TextDarkMode,
    surface             = SurfaceLightMode,
    onSurface           = TextDarkMode,
    surfaceVariant      = SurfaceVariantLight,
    onSurfaceVariant    = TextGrayMode,
    error               = ErrorLight,
    onError             = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary             = OrangeMain,
    onPrimary           = Color.White,
    secondary           = OrangeDark,
    onSecondary         = Color.White,
    tertiary            = OrangeLight,
    onTertiary          = TextDarkMode,
    background          = BgDarkMode,
    onBackground        = TextLightMode,
    surface             = SurfaceDarkMode,
    onSurface           = TextLightMode,
    surfaceVariant      = SurfaceVariantDark,
    onSurfaceVariant    = TextGrayDarkMode,
    error               = ErrorDark,
    onError             = Color.White,
)

@Composable
fun EventTicketAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = Typography,
        content     = content
    )
}