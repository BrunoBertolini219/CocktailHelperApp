package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.CocktailHelperAppTheme

/**
 * Display-only tags for a drink's attributes (alcoholic / category / glass).
 * Rendered as non-interactive pills so screen readers don't treat them as buttons.
 */
@Composable
fun DrinkInfoChips(
    alcoholic: String?,
    category: String?,
    glass: String?,
    modifier: Modifier = Modifier
) {
    val tags = listOfNotNull(alcoholic, category, glass).filter { it.isNotBlank() }
    if (tags.isEmpty()) return

    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        tags.forEach { tag -> InfoChip(text = tag) }
    }
}

@Composable
private fun InfoChip(text: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        CocktailText(
            text = text,
            style = CocktailTextStyle.Caption,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun DrinkInfoChipsPreview() {
    CocktailHelperAppTheme(dynamicColor = false) {
        Surface {
            DrinkInfoChips(
                alcoholic = "Alcoholic",
                category = "Ordinary Drink",
                glass = "Cocktail glass",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
