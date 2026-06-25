package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkListItem
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.EmptyState
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkFavoritesList(
    drinks: List<DrinkSummary>,
    onDrinkClick: (DrinkSummary) -> Unit,
    onDelete: (DrinkSummary) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    if (drinks.isEmpty()) {
        EmptyState(
            icon = Icons.Filled.FavoriteBorder,
            title = stringResource(R.string.no_favorites),
            subtitle = stringResource(R.string.favorites_empty_subtitle),
            modifier = modifier
        )
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(Spacing.sm)
    ) {
        items(drinks, key = { it.id }) { drink ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        onDelete(drink)
                        true
                    } else {
                        false
                    }
                }
            )
            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                backgroundContent = { DeleteBackground() },
                modifier = Modifier.animateItem()
            ) {
                Surface(color = MaterialTheme.colorScheme.surface) {
                    DrinkListItem(
                        drink = drink,
                        onClick = onDrinkClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun DeleteBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.errorContainer)
            .padding(horizontal = Spacing.lg),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.delete_drink),
            tint = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}
