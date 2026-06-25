package br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DrinkListDto(
    val drinks: List<DrinkDetailDto>? = null
)
