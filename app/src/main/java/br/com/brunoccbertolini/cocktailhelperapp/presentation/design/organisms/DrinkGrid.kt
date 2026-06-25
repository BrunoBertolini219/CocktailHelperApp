package br.com.brunoccbertolini.cocktailhelperapp.presentation.design.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkCard
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkGridSkeleton
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.EmptyState
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.FeaturedDrinkCard
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinkGrid(
    drinks: List<DrinkSummary>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    error: String?,
    onRefresh: () -> Unit,
    onDrinkClick: (DrinkSummary) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    favoriteIds: Set<String> = emptySet(),
    onToggleFavorite: ((DrinkSummary) -> Unit)? = null
) {
    when {
        isLoading && drinks.isEmpty() -> DrinkGridSkeleton(modifier = modifier, contentPadding = contentPadding)

        error != null && drinks.isEmpty() -> EmptyState(
            icon = Icons.Filled.Warning,
            title = error,
            actionLabel = stringResource(R.string.retry),
            onAction = onRefresh,
            modifier = modifier
        )

        drinks.isEmpty() -> EmptyState(
            icon = Icons.Filled.Info,
            title = stringResource(R.string.empty_list),
            modifier = modifier
        )

        else -> PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(170.dp),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                verticalArrangement = Arrangement.spacedBy(Spacing.md),
                modifier = Modifier.fillMaxSize()
            ) {
                val featured = drinks.first()
                item(span = { GridItemSpan(maxLineSpan) }, key = "featured-${featured.id}") {
                    FeaturedDrinkCard(
                        drink = featured,
                        onClick = onDrinkClick,
                        modifier = Modifier.animateItem(),
                        isFavorite = favoriteIds.contains(featured.id),
                        onToggleFavorite = onToggleFavorite?.let { cb -> { cb(featured) } }
                    )
                }
                items(drinks.drop(1), key = { it.id }) { drink ->
                    DrinkCard(
                        drink = drink,
                        onClick = onDrinkClick,
                        modifier = Modifier.animateItem(),
                        isFavorite = favoriteIds.contains(drink.id),
                        onToggleFavorite = onToggleFavorite?.let { cb -> { cb(drink) } }
                    )
                }
            }
        }
    }
}
