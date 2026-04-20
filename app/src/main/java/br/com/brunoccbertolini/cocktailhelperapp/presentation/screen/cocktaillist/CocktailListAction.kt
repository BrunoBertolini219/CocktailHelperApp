package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

sealed interface CocktailListAction {
    data object Refresh : CocktailListAction
    data class SelectTab(val index: Int) : CocktailListAction
    data class DrinkClicked(val drink: DrinkSummary) : CocktailListAction
}
