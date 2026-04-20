package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

enum class CocktailTextStyle { Title, Body, Caption }

@Composable
fun CocktailText(
    text: String,
    modifier: Modifier = Modifier,
    style: CocktailTextStyle = CocktailTextStyle.Body,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip
) {
    val textStyle: TextStyle = when (style) {
        CocktailTextStyle.Title -> MaterialTheme.typography.titleMedium
        CocktailTextStyle.Body -> MaterialTheme.typography.bodyMedium
        CocktailTextStyle.Caption -> MaterialTheme.typography.bodySmall
    }
    Text(
        text = text,
        style = textStyle,
        maxLines = maxLines,
        overflow = overflow,
        modifier = modifier
    )
}
