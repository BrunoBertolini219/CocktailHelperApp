package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.search

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

sealed interface SearchAction {
    data class QueryChanged(val query: String) : SearchAction
    data class SearchTypeChanged(val type: SearchType) : SearchAction
    data class DrinkClicked(val drink: DrinkSummary) : SearchAction
}
