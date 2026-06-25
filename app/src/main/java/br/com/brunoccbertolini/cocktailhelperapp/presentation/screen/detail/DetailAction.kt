package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem

sealed interface DetailAction {
    data object Retry : DetailAction
    data object ToggleFavorite : DetailAction
    data class SetMeasureSystem(val system: MeasureSystem) : DetailAction
    data object NavigateUp : DetailAction
}
