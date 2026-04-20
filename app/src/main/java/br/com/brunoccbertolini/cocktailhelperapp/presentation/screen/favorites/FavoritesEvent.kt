package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.favorites

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

sealed interface FavoritesEvent {
    data class NavigateToDetail(val drink: DrinkSummary) : FavoritesEvent
}
