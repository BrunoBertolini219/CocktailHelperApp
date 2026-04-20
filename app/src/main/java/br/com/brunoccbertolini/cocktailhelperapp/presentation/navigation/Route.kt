package br.com.brunoccbertolini.cocktailhelperapp.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable data object CocktailList : Route
    @Serializable data object Search : Route
    @Serializable data object Favorites : Route
    @Serializable data object Random : Route
    @Serializable data class Detail(
        val idDrink: String,
        val strDrink: String,
        val strDrinkThumb: String?
    ) : Route
}
