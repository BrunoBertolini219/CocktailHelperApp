package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailButton
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.CocktailHelperAppTheme
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

/** Centered illustration + message used for empty, prompt, and error states. */
@Composable
fun EmptyState(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(96.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(44.dp)
                )
            }
        }
        Spacer(Modifier.height(Spacing.lg))
        CocktailText(text = title, style = CocktailTextStyle.TitleSmall, textAlign = TextAlign.Center)
        subtitle?.let {
            Spacer(Modifier.height(Spacing.xs))
            CocktailText(
                text = it,
                style = CocktailTextStyle.Body,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(Spacing.lg))
            CocktailButton(text = actionLabel, onClick = onAction)
        }
    }
}

@PreviewLightDark
@Composable
private fun EmptyStatePreview() {
    CocktailHelperAppTheme(dynamicColor = false) {
        Surface {
            EmptyState(
                icon = Icons.Filled.FavoriteBorder,
                title = "No favorites yet",
                subtitle = "Tap the heart on any drink to keep it here.",
                actionLabel = "Browse drinks",
                onAction = {}
            )
        }
    }
}
