package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.organisms.DrinkFavoritesList
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.ObserveAsEvents
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

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

    Column(modifier = Modifier.fillMaxSize()) {
        CocktailText(
            text = stringResource(R.string.favorites),
            style = CocktailTextStyle.Headline,
            modifier = Modifier.padding(
                start = Spacing.lg,
                end = Spacing.lg,
                top = contentPadding.calculateTopPadding() + Spacing.lg,
                bottom = Spacing.sm
            )
        )
        DrinkFavoritesList(
            drinks = state.drinks,
            onDrinkClick = { viewModel.onAction(FavoritesAction.DrinkClicked(it)) },
            onDelete = { viewModel.onAction(FavoritesAction.DeleteDrink(it)) },
            contentPadding = PaddingValues(
                start = Spacing.lg,
                end = Spacing.lg,
                top = Spacing.xs,
                bottom = contentPadding.calculateBottomPadding() + Spacing.lg
            ),
            modifier = Modifier.weight(1f)
        )
    }
}
