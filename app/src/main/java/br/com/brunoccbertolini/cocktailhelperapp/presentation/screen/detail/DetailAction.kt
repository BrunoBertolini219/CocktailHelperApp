package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

sealed interface DetailAction {
    data object Retry : DetailAction
    data object SaveFavorite : DetailAction
    data object NavigateUp : DetailAction
}
