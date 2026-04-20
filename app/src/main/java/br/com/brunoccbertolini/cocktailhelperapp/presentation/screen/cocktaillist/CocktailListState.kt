package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

data class CocktailListState(
    val selectedTab: Int = 0,
    val alcoholicDrinks: List<DrinkSummary> = emptyList(),
    val nonAlcoholicDrinks: List<DrinkSummary> = emptyList(),
    val alcoholicLoading: Boolean = true,
    val nonAlcoholicLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val alcoholicError: String? = null,
    val nonAlcoholicError: String? = null
)
