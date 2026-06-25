package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem

sealed interface RandomDrinkAction {
    data object Refresh : RandomDrinkAction
    data object ToggleFavorite : RandomDrinkAction
    data class SetMeasureSystem(val system: MeasureSystem) : RandomDrinkAction
}
