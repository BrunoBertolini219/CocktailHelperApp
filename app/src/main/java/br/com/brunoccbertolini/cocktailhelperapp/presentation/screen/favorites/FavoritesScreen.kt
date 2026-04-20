package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.favorites

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.organisms.DrinkFavoritesList
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents

@Composable
fun FavoritesScreen(
    onNavigateToDetail: (DrinkSummary) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is FavoritesEvent.NavigateToDetail -> onNavigateToDetail(event.drink)
        }
    }

    DrinkFavoritesList(
        drinks = state.drinks,
        onDrinkClick = { viewModel.onAction(FavoritesAction.DrinkClicked(it)) },
        onDelete = { viewModel.onAction(FavoritesAction.DeleteDrink(it)) },
        contentPadding = contentPadding
    )
}
