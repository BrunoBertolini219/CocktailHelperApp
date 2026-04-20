package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.search

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

enum class SearchType { Name, Ingredient }

data class SearchState(
    val query: String = "",
    val searchType: SearchType = SearchType.Name,
    val isLoading: Boolean = false,
    val drinks: List<DrinkSummary>? = null,
    val error: String? = null
)
