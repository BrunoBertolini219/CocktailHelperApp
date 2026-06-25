package br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CocktailListDto(
    val drinks: List<DrinkSummaryDto>? = null
)
