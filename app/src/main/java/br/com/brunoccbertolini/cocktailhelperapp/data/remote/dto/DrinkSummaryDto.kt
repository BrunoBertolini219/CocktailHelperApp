package br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrinkSummaryDto(
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String? = null
)
