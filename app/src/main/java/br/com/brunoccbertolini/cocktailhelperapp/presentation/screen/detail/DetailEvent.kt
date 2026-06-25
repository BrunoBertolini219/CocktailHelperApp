package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.UiText

sealed interface DetailEvent {
    data object NavigateUp : DetailEvent
    data class ShowSnackbar(val message: UiText) : DetailEvent
}
