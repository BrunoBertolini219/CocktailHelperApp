package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.organisms.DrinkGrid
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents

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
    val tabs = listOf(stringResource(R.string.alcoholic), stringResource(R.string.non_alcoholic))

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = state.selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = state.selectedTab == index,
                    onClick = { onAction(CocktailListAction.SelectTab(index)) },
                    text = { Text(title) }
                )
            }
        }

        val drinks = if (state.selectedTab == 0) state.alcoholicDrinks else state.nonAlcoholicDrinks
        val isLoading = if (state.selectedTab == 0) state.alcoholicLoading else state.nonAlcoholicLoading
        val error = if (state.selectedTab == 0) state.alcoholicError else state.nonAlcoholicError

        DrinkGrid(
            drinks = drinks,
            isLoading = isLoading,
            isRefreshing = state.isRefreshing,
            error = error,
            onRefresh = { onAction(CocktailListAction.Refresh) },
            onDrinkClick = { onAction(CocktailListAction.DrinkClicked(it)) },
            contentPadding = contentPadding
        )
    }
}
