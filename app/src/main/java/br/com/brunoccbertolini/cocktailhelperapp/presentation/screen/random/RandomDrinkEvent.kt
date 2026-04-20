package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

sealed interface RandomDrinkEvent {
    data class ShowSnackbar(val message: String) : RandomDrinkEvent
}
