package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

sealed interface RandomDrinkAction {
    data object Refresh : RandomDrinkAction
    data object SaveFavorite : RandomDrinkAction
}
