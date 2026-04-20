package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

sealed interface CocktailListEvent {
    data class NavigateToDetail(val drink: DrinkSummary) : CocktailListEvent
}
