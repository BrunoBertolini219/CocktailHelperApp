package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.favorites

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary

data class FavoritesState(
    val drinks: List<DrinkSummary> = emptyList()
)
