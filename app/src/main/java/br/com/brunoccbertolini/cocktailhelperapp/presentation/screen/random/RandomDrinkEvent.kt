package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.UiText

sealed interface RandomDrinkEvent {
    data class ShowSnackbar(val message: UiText) : RandomDrinkEvent
}
