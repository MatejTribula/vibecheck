package dk.sdu.vibecheck.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

enum class ContrastLevel {
    Normal,
    Medium,
    High
}

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    surfaceTint = LightSurfaceTint,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    inversePrimary = LightInversePrimary,
    surfaceDim = LightSurfaceDim,
    surfaceBright = LightSurfaceBright,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    surfaceTint = DarkSurfaceTint,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    inversePrimary = DarkInversePrimary,
    surfaceDim = DarkSurfaceDim,
    surfaceBright = DarkSurfaceBright,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest
)

val LightHighContrastColorScheme = lightColorScheme(
    primary = LightHighPrimary,
    surfaceTint = LightSurfaceTint,
    onPrimary = LightOnPrimary,
    primaryContainer = LightHighPrimaryContainer,
    onPrimaryContainer = LightOnPrimary,
    secondary = LightHighSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightHighSecondaryContainer,
    onSecondaryContainer = LightOnSecondary,
    tertiary = LightHighTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightHighTertiaryContainer,
    onTertiaryContainer = LightOnTertiary,
    error = LightHighError,
    onError = LightOnError,
    errorContainer = LightHighErrorContainer,
    onErrorContainer = LightOnError,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightHighOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightHighOnSurfaceVariant,
    outline = LightHighOutline,
    outlineVariant = LightHighOutlineVariant,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightOnPrimary,
    inversePrimary = LightInversePrimary,
    surfaceDim = LightHighSurfaceDim,
    surfaceBright = LightSurfaceBright,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightHighSurfaceContainerLow,
    surfaceContainer = LightHighSurfaceContainer,
    surfaceContainerHigh = LightHighSurfaceContainerHigh,
    surfaceContainerHighest = LightHighSurfaceContainerHighest
)

val LightMediumContrastColorScheme = lightColorScheme(
    primary = LightMediumPrimary,
    surfaceTint = LightSurfaceTint,
    onPrimary = LightOnPrimary,
    primaryContainer = LightMediumPrimaryContainer,
    onPrimaryContainer = LightOnPrimary,
    secondary = LightMediumSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightMediumSecondaryContainer,
    onSecondaryContainer = LightOnSecondary,
    tertiary = LightMediumTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightMediumTertiaryContainer,
    onTertiaryContainer = LightOnTertiary,
    error = LightMediumError,
    onError = LightOnError,
    errorContainer = LightMediumErrorContainer,
    onErrorContainer = LightOnSecondary,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightMediumOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightMediumOnSurfaceVariant,
    outline = LightMediumOutline,
    outlineVariant = LightMediumOutlineVariant,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    inversePrimary = LightInversePrimary,
    surfaceDim = LightMediumSurfaceDim,
    surfaceBright = LightSurfaceBright,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainer = LightMediumSurfaceContainer,
    surfaceContainerHigh = LightMediumSurfaceContainerHigh,
    surfaceContainerHighest = LightMediumSurfaceContainerHighest
)

val DarkHighContrastColorScheme = darkColorScheme(
    primary = DarkHighPrimary,
    surfaceTint = DarkSurfaceTint,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkHighPrimaryContainer,
    onPrimaryContainer = DarkHighOnPrimaryContainer,
    secondary = DarkHighSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkHighSecondaryContainer,
    onSecondaryContainer = DarkHighOnSecondaryContainer,
    tertiary = DarkHighTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkHighTertiaryContainer,
    onTertiaryContainer = DarkHighOnTertiaryContainer,
    error = DarkHighError,
    onError = DarkOnError,
    errorContainer = DarkHighErrorContainer,
    onErrorContainer = DarkHighOnErrorContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkHighOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkHighOnSurfaceVariant,
    outline = DarkHighOutline,
    outlineVariant = DarkHighOutlineVariant,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkOnSecondary,
    inversePrimary = DarkInversePrimary,
    surfaceDim = DarkSurfaceDim,
    surfaceBright = DarkHighSurfaceBright,
    surfaceContainerLowest = DarkHighSurfaceContainerLowest,
    surfaceContainerLow = DarkHighSurfaceContainerLow,
    surfaceContainer = DarkHighSurfaceContainer,
    surfaceContainerHigh = DarkHighSurfaceContainerHigh,
    surfaceContainerHighest = DarkHighSurfaceContainerHighest
)

val DarkMediumContrastColorScheme = darkColorScheme(
    primary = DarkMediumPrimary,
    surfaceTint = DarkSurfaceTint,
    onPrimary = DarkMediumOnPrimary,
    primaryContainer = DarkMediumPrimaryContainer,
    onPrimaryContainer = LightOnPrimary,
    secondary = DarkMediumSecondary,
    onSecondary = DarkMediumOnSecondary,
    secondaryContainer = DarkMediumSecondaryContainer,
    onSecondaryContainer = LightOnPrimary,
    tertiary = DarkMediumTertiary,
    onTertiary = DarkMediumOnTertiary,
    tertiaryContainer = DarkMediumTertiaryContainer,
    onTertiaryContainer = LightOnPrimary,
    error = DarkMediumError,
    onError = DarkMediumOnError,
    errorContainer = DarkMediumErrorContainer,
    onErrorContainer = LightOnPrimary,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkHighOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMediumOnSurfaceVariant,
    outline = DarkMediumOutline,
    outlineVariant = DarkMediumOutlineVariant,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    inversePrimary = DarkInversePrimary,
    surfaceDim = DarkSurfaceDim,
    surfaceBright = DarkMediumSurfaceBright,
    surfaceContainerLowest = DarkMediumSurfaceContainerLowest,
    surfaceContainerLow = DarkMediumSurfaceContainerLow,
    surfaceContainer = DarkMediumSurfaceContainer,
    surfaceContainerHigh = DarkMediumSurfaceContainerHigh,
    surfaceContainerHighest = DarkMediumSurfaceContainerHighest
)

@Composable
fun VibecheckTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    contrastLevel: ContrastLevel = ContrastLevel.Normal,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && contrastLevel == ContrastLevel.Normal -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> {
            when (contrastLevel) {
                ContrastLevel.High -> DarkHighContrastColorScheme
                ContrastLevel.Medium ->DarkMediumContrastColorScheme
                ContrastLevel.Normal -> DarkColorScheme
            }
        }
        else -> {
            when (contrastLevel) {
                ContrastLevel.High -> LightHighContrastColorScheme
                ContrastLevel.Medium -> LightMediumContrastColorScheme
                ContrastLevel.Normal -> LightColorScheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = VibeTypography,
        content = content
    )
}
