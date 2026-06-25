package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailImage
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.util.bottomScrim
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.CocktailHelperAppTheme
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

/** A vivid heart color that stays legible over any photo. */
internal val FavoriteRose = Color(0xFFFF5A7A)

/** Editorial overlay card: full-bleed image, gradient scrim, name on top, optional quick-favorite. */
@Composable
fun DrinkCard(
    drink: DrinkSummary,
    onClick: (DrinkSummary) -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "drinkCardScale")

    Card(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable(interactionSource = interaction, indication = null) { onClick(drink) },
        shape = MaterialTheme.shapes.large
    ) {
        Box(modifier = Modifier.aspectRatio(0.82f)) {
            CocktailImage(
                url = drink.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier.matchParentSize()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .bottomScrim(endAlpha = 0.85f, heightFraction = 0.75f)
            )
            CocktailText(
                text = drink.name,
                style = CocktailTextStyle.TitleSmall,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(Spacing.md)
            )
            if (onToggleFavorite != null) {
                FavoriteHeart(
                    isFavorite = isFavorite,
                    onToggle = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(Spacing.sm)
                )
            }
        }
    }
}

/** Small circular heart overlay for cards (legible over imagery via a dark scrim). */
@Composable
internal fun FavoriteHeart(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(Color.Black.copy(alpha = 0.32f))
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = stringResource(
                if (isFavorite) R.string.remove_from_favorites else R.string.save_to_favorites
            ),
            tint = if (isFavorite) FavoriteRose else Color.White,
            modifier = Modifier.size(18.dp)
        )
    }
}

@PreviewLightDark
@Composable
private fun DrinkCardPreview() {
    CocktailHelperAppTheme(dynamicColor = false) {
        Surface {
            DrinkCard(
                drink = DrinkSummary("1", "Strawberry Daiquiri", null),
                onClick = {},
                modifier = Modifier
                    .width(180.dp)
                    .padding(Spacing.sm),
                isFavorite = true,
                onToggleFavorite = {}
            )
        }
    }
}
