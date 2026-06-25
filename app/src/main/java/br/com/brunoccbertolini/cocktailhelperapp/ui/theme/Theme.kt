package br.com.brunoccbertolini.cocktailhelperapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = BerryLight,
    onPrimary = OnBerryLight,
    primaryContainer = BerryContainerLight,
    onPrimaryContainer = OnBerryContainerLight,
    secondary = CitrusLight,
    onSecondary = OnCitrusLight,
    secondaryContainer = CitrusContainerLight,
    onSecondaryContainer = OnCitrusContainerLight,
    tertiary = MintLight,
    onTertiary = OnMintLight,
    tertiaryContainer = MintContainerLight,
    onTertiaryContainer = OnMintContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight
)

private val DarkColors = darkColorScheme(
    primary = BerryDark,
    onPrimary = OnBerryDark,
    primaryContainer = BerryContainerDark,
    onPrimaryContainer = OnBerryContainerDark,
    secondary = CitrusDark,
    onSecondary = OnCitrusDark,
    secondaryContainer = CitrusContainerDark,
    onSecondaryContainer = OnCitrusContainerDark,
    tertiary = MintDark,
    onTertiary = OnMintDark,
    tertiaryContainer = MintContainerDark,
    onTertiaryContainer = OnMintContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark
)

/**
 * App theme. Honors the system dark setting, opts into Material You dynamic color on
 * Android 12+, and otherwise falls back to the branded berry/citrus/mint scheme.
 */
@Composable
fun CocktailHelperAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = CocktailTypography,
        shapes = CocktailShapes,
        content = content
    )
}
