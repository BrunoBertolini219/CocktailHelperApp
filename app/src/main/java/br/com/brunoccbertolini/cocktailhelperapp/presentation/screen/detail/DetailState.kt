package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail

data class DetailState(
    val drinkName: String = "",
    val isLoading: Boolean = false,
    val drink: DrinkDetail? = null,
    val error: String? = null
)
