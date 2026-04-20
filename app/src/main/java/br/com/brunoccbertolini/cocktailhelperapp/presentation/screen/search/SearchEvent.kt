package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.search

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

sealed interface SearchEvent {
    data class NavigateToDetail(val drink: DrinkSummary) : SearchEvent
}
