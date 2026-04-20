package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.Ingredient
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle

@Composable
fun IngredientRow(ingredient: Ingredient, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        CocktailText(
            text = "• ${ingredient.name}",
            style = CocktailTextStyle.Body,
            modifier = Modifier.weight(1f)
        )
        if (!ingredient.measure.isNullOrBlank()) {
            Spacer(modifier = Modifier.width(8.dp))
            CocktailText(
                text = ingredient.measure,
                style = CocktailTextStyle.Caption
            )
        }
    }
}
