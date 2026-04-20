package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail

data class RandomDrinkState(
    val isLoading: Boolean = false,
    val drink: DrinkDetail? = null,
    val error: String? = null
)
