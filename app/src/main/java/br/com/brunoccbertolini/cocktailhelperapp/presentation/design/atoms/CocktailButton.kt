package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.CocktailHelperAppTheme

@Composable
fun CocktailButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick, modifier = modifier) {
        Text(text)
    }
}

@Composable
fun CocktailTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier) {
        Text(text)
    }
}

@PreviewLightDark
@Composable
private fun CocktailButtonPreview() {
    CocktailHelperAppTheme(dynamicColor = false) {
        Surface {
            CocktailButton(text = "Retry", onClick = {}, modifier = Modifier.padding(16.dp))
        }
    }
}
