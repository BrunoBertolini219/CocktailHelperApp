package br.com.brunoccbertolini.cocktailhelperapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontWeight

private val Default = Typography()

/** Material 3 type scale with slightly bolder titles for brand character. */
val CocktailTypography = Default.copy(
    titleLarge = Default.titleLarge.copy(fontWeight = FontWeight.Bold),
    titleMedium = Default.titleMedium.copy(fontWeight = FontWeight.SemiBold),
    headlineSmall = Default.headlineSmall.copy(fontWeight = FontWeight.Bold)
)
