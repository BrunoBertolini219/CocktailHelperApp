package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkCard
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkGridSkeleton
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.EmptyState
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.SearchField
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

@Composable
fun SearchScreen(
    onNavigateToDetail: (DrinkSummary) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SearchEvent.NavigateToDetail -> onNavigateToDetail(event.drink)
        }
    }

    SearchContent(
        state = state,
        onAction = viewModel::onAction,
        contentPadding = contentPadding
    )
}

@Composable
private fun SearchContent(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
    contentPadding: PaddingValues
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Spacing.lg,
                    end = Spacing.lg,
                    top = contentPadding.calculateTopPadding() + Spacing.lg,
                    bottom = Spacing.sm
                )
        ) {
            CocktailText(text = stringResource(R.string.search), style = CocktailTextStyle.Headline)
            Spacer(Modifier.height(Spacing.md))
            SearchField(
                query = state.query,
                onQueryChange = { onAction(SearchAction.QueryChanged(it)) }
            )
            Spacer(Modifier.height(Spacing.sm))
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                FilterChip(
                    selected = state.searchType == SearchType.Name,
                    onClick = { onAction(SearchAction.SearchTypeChanged(SearchType.Name)) },
                    label = { Text(stringResource(R.string.by_name)) }
                )
                FilterChip(
                    selected = state.searchType == SearchType.Ingredient,
                    onClick = { onAction(SearchAction.SearchTypeChanged(SearchType.Ingredient)) },
                    label = { Text(stringResource(R.string.by_ingredient)) }
                )
            }
        }

        val gridPadding = PaddingValues(
            start = Spacing.lg,
            end = Spacing.lg,
            top = Spacing.xs,
            bottom = contentPadding.calculateBottomPadding() + Spacing.lg
        )

        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            when {
                state.query.isBlank() -> EmptyState(
                    icon = Icons.Filled.Search,
                    title = stringResource(R.string.search_prompt)
                )

                state.isLoading -> DrinkGridSkeleton(contentPadding = gridPadding)

                state.error != null -> EmptyState(
                    icon = Icons.Filled.Warning,
                    title = state.error.asString(),
                    actionLabel = stringResource(R.string.retry),
                    onAction = { onAction(SearchAction.QueryChanged(state.query)) }
                )

                state.drinks != null && state.drinks.isEmpty() -> EmptyState(
                    icon = Icons.Filled.Search,
                    title = stringResource(R.string.no_results),
                    subtitle = stringResource(R.string.search_no_results_subtitle)
                )

                state.drinks != null -> LazyVerticalGrid(
                    columns = GridCells.Adaptive(170.dp),
                    contentPadding = gridPadding,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                    verticalArrangement = Arrangement.spacedBy(Spacing.md),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.drinks, key = { it.id }) { drink ->
                        DrinkCard(
                            drink = drink,
                            onClick = { onAction(SearchAction.DrinkClicked(drink)) },
                            modifier = Modifier.animateItem(),
                            isFavorite = state.favoriteIds.contains(drink.id),
                            onToggleFavorite = { onAction(SearchAction.ToggleFavorite(drink)) }
                        )
                    }
                }
            }
        }
    }
}
