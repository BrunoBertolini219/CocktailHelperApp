package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.UiText

data class RandomDrinkState(
    val isLoading: Boolean = false,
    val drink: DrinkDetail? = null,
    val isFavorite: Boolean = false,
    val measureSystem: MeasureSystem = MeasureSystem.Original,
    val error: UiText? = null
)
