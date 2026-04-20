package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.LoadingIndicator
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.DrinkCard
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.ErrorMessage
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.SearchField
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents

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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        SearchField(
            query = state.query,
            onQueryChange = { onAction(SearchAction.QueryChanged(it)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilterChip(
                selected = state.searchType == SearchType.Name,
                onClick = { onAction(SearchAction.SearchTypeChanged(SearchType.Name)) },
                label = { CocktailText(text = stringResource(R.string.by_name)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = state.searchType == SearchType.Ingredient,
                onClick = { onAction(SearchAction.SearchTypeChanged(SearchType.Ingredient)) },
                label = { CocktailText(text = stringResource(R.string.by_ingredient)) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        when {
            state.query.isBlank() -> CocktailText(
                text = stringResource(R.string.search_prompt),
                modifier = Modifier
                    .padding(top = 32.dp)
                    .align(Alignment.CenterHorizontally)
            )
            state.isLoading -> LoadingIndicator()
            state.error != null -> ErrorMessage(
                message = state.error,
                onRetry = { onAction(SearchAction.QueryChanged(state.query)) }
            )
            state.drinks != null && state.drinks.isEmpty() -> CocktailText(
                text = stringResource(R.string.no_results),
                modifier = Modifier
                    .padding(top = 32.dp)
                    .align(Alignment.CenterHorizontally)
            )
            state.drinks != null -> LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                contentPadding = contentPadding
            ) {
                items(state.drinks, key = { it.id }) { drink ->
                    DrinkCard(
                        drink = drink,
                        onClick = { onAction(SearchAction.DrinkClicked(drink)) },
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}
