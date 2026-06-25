package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.PreviewLightDark
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.Ingredient
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.convertMeasure
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.CocktailHelperAppTheme
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

/**
 * Ingredient list with a unit toggle (oz ⇄ ml) in the header and tap-to-check rows
 * (checked items strike through and dim, so you can track what you have on hand).
 * Check state and the chosen unit are hoisted to the caller.
 */
@Composable
fun IngredientsSection(
    ingredients: List<Ingredient>,
    measureSystem: MeasureSystem,
    onMeasureSystemChange: (MeasureSystem) -> Unit,
    isChecked: (Int) -> Boolean,
    onToggleChecked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        SectionHeader(
            title = "${stringResource(R.string.ingredients)} · ${ingredients.size}",
            trailing = { UnitToggle(measureSystem, onMeasureSystemChange) }
        )
        Spacer(Modifier.height(Spacing.sm))
        ingredients.forEachIndexed { index, ingredient ->
            CheckableIngredientRow(
                ingredient = ingredient,
                measureSystem = measureSystem,
                checked = isChecked(index),
                onToggle = { onToggleChecked(index) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitToggle(system: MeasureSystem, onChange: (MeasureSystem) -> Unit) {
    SingleChoiceSegmentedButtonRow {
        SegmentedButton(
            selected = system == MeasureSystem.Original,
            onClick = { onChange(MeasureSystem.Original) },
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
        ) { Text(stringResource(R.string.oz)) }
        SegmentedButton(
            selected = system == MeasureSystem.Metric,
            onClick = { onChange(MeasureSystem.Metric) },
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
        ) { Text(stringResource(R.string.ml)) }
    }
}

@Composable
private fun CheckableIngredientRow(
    ingredient: Ingredient,
    measureSystem: MeasureSystem,
    checked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .clickable { onToggle() }
            .padding(vertical = Spacing.xxs),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = { onToggle() })
        Spacer(Modifier.width(Spacing.xs))
        val decoration = if (checked) TextDecoration.LineThrough else null
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodyLarge,
            color = if (checked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            textDecoration = decoration,
            modifier = Modifier.weight(1f)
        )
        val measure = convertMeasure(ingredient.measure, measureSystem)
        if (!measure.isNullOrBlank()) {
            Spacer(Modifier.width(Spacing.sm))
            Text(
                text = measure,
                style = MaterialTheme.typography.labelLarge,
                color = if (checked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary,
                textDecoration = decoration
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun IngredientsSectionPreview() {
    CocktailHelperAppTheme(dynamicColor = false) {
        Surface {
            var system by remember { mutableStateOf(MeasureSystem.Original) }
            val checked = remember { mutableStateMapOf<Int, Boolean>() }
            IngredientsSection(
                ingredients = listOf(
                    Ingredient("White Rum", "2 oz"),
                    Ingredient("Lime Juice", "1/2 oz"),
                    Ingredient("Mint", null)
                ),
                measureSystem = system,
                onMeasureSystemChange = { system = it },
                isChecked = { checked[it] == true },
                onToggleChecked = { checked[it] = checked[it] != true },
                modifier = Modifier.padding(Spacing.lg)
            )
        }
    }
}
