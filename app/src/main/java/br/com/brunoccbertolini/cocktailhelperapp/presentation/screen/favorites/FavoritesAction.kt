package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.favorites

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

sealed interface FavoritesAction {
    data class DrinkClicked(val drink: DrinkSummary) : FavoritesAction
    data class DeleteDrink(val drink: DrinkSummary) : FavoritesAction
}
