package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.organisms.DrinkGrid
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

@Composable
fun CocktailListScreen(
    onNavigateToDetail: (DrinkSummary) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: CocktailListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is CocktailListEvent.NavigateToDetail -> onNavigateToDetail(event.drink)
        }
    }

    CocktailListContent(
        state = state,
        onAction = viewModel::onAction,
        contentPadding = contentPadding
    )
}

@Composable
private fun CocktailListContent(
    state: CocktailListState,
    onAction: (CocktailListAction) -> Unit,
    contentPadding: PaddingValues
) {
    val drinks = if (state.selectedTab == 0) state.alcoholicDrinks else state.nonAlcoholicDrinks
    val isLoading = if (state.selectedTab == 0) state.alcoholicLoading else state.nonAlcoholicLoading
    val error = (if (state.selectedTab == 0) state.alcoholicError else state.nonAlcoholicError)?.asString()

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Spacing.lg,
                    end = Spacing.lg,
                    top = contentPadding.calculateTopPadding() + Spacing.lg,
                    bottom = Spacing.md
                )
        ) {
            CocktailText(text = stringResource(R.string.discover_title), style = CocktailTextStyle.Headline)
            Spacer(Modifier.height(Spacing.xs))
            CocktailText(
                text = stringResource(R.string.discover_subtitle),
                style = CocktailTextStyle.Body,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(Spacing.md))
            Row(horizontalArrangement = Arrangement.spacedBy(Spacing.sm)) {
                CategoryChip(
                    label = stringResource(R.string.alcoholic),
                    selected = state.selectedTab == 0,
                    onClick = { onAction(CocktailListAction.SelectTab(0)) }
                )
                CategoryChip(
                    label = stringResource(R.string.non_alcoholic),
                    selected = state.selectedTab == 1,
                    onClick = { onAction(CocktailListAction.SelectTab(1)) }
                )
            }
        }

        DrinkGrid(
            drinks = drinks,
            isLoading = isLoading,
            isRefreshing = state.isRefreshing,
            error = error,
            onRefresh = { onAction(CocktailListAction.Refresh) },
            onDrinkClick = { onAction(CocktailListAction.DrinkClicked(it)) },
            contentPadding = PaddingValues(
                start = Spacing.lg,
                end = Spacing.lg,
                top = Spacing.xs,
                bottom = contentPadding.calculateBottomPadding() + Spacing.lg
            ),
            favoriteIds = state.favoriteIds,
            onToggleFavorite = { onAction(CocktailListAction.ToggleFavorite(it)) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else null
    )
}
