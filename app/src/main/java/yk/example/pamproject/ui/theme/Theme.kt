package yk.example.pamproject.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = CanteenGreenPrimaryDark,
    onPrimary = CanteenGreenOnPrimaryDark,
    primaryContainer = CanteenGreenContainerDark,
    onPrimaryContainer = CanteenGreenOnContainerDark,
    secondary = CanteenWarmSecondaryDark,
    onSecondary = CanteenWarmOnSecondaryDark,
    secondaryContainer = CanteenWarmContainerDark,
    onSecondaryContainer = CanteenWarmOnContainerDark,
    tertiary = CanteenAmberTertiaryDark,
    onTertiary = CanteenAmberOnTertiaryDark,
    tertiaryContainer = CanteenAmberContainerDark,
    onTertiaryContainer = CanteenAmberOnTertiaryContainerDark,
    background = Color(0xFF121512),
    surface = Color(0xFF1A1E1A),
    surfaceVariant = Color(0xFF282D28),
    outline = Color(0xFF8C928B),
    outlineVariant = Color(0xFF3E443E),
    onBackground = Color(0xFFE3E5E0),
    onSurface = Color(0xFFE3E5E0),
    onSurfaceVariant = Color(0xFFC1C7BF),
)

private val LightColorScheme = lightColorScheme(
    primary = CanteenGreenPrimary,
    onPrimary = CanteenGreenOnPrimary,
    primaryContainer = CanteenGreenContainer,
    onPrimaryContainer = CanteenGreenOnContainer,
    secondary = CanteenWarmSecondary,
    onSecondary = CanteenWarmOnSecondary,
    secondaryContainer = CanteenWarmContainer,
    onSecondaryContainer = CanteenWarmOnContainer,
    tertiary = CanteenAmberTertiary,
    onTertiary = CanteenAmberOnTertiary,
    tertiaryContainer = CanteenAmberContainer,
    onTertiaryContainer = CanteenAmberOnContainer,
    background = CanteenBackgroundLight,
    surface = CanteenSurfaceLight,
    surfaceVariant = CanteenSurfaceVariantLight,
    outline = CanteenOutlineLight,
    outlineVariant = CanteenOutlineVariantLight,
    onBackground = CanteenOnBackgroundLight,
    onSurface = CanteenOnSurfaceLight,
    onSurfaceVariant = CanteenOnSurfaceVariantLight,
)

@Composable
fun PamprojectTheme(
    darkTheme: Boolean = false,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
