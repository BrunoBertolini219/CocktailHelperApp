package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.CocktailHelperAppTheme

enum class CocktailTextStyle { Headline, Title, TitleSmall, Body, Label, Caption }

@Composable
fun CocktailText(
    text: String,
    modifier: Modifier = Modifier,
    style: CocktailTextStyle = CocktailTextStyle.Body,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    textAlign: TextAlign? = null
) {
    val textStyle: TextStyle = when (style) {
        CocktailTextStyle.Headline -> MaterialTheme.typography.headlineSmall
        CocktailTextStyle.Title -> MaterialTheme.typography.titleLarge
        CocktailTextStyle.TitleSmall -> MaterialTheme.typography.titleMedium
        CocktailTextStyle.Body -> MaterialTheme.typography.bodyMedium
        CocktailTextStyle.Label -> MaterialTheme.typography.labelLarge
        CocktailTextStyle.Caption -> MaterialTheme.typography.bodySmall
    }
    Text(
        text = text,
        style = textStyle,
        color = color,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        modifier = modifier
    )
}

@PreviewLightDark
@Composable
private fun CocktailTextPreview() {
    CocktailHelperAppTheme(dynamicColor = false) {
        Surface {
            CocktailText(
                text = "Mojito",
                style = CocktailTextStyle.Title,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
