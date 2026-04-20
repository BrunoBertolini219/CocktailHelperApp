package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

sealed interface DetailEvent {
    data object NavigateUp : DetailEvent
    data class ShowSnackbar(val message: String) : DetailEvent
}
