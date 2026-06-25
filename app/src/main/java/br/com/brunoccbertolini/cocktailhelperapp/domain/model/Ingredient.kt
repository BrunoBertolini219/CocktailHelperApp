package br.com.brunoccbertolini.cocktailhelperapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val name: String,
    val measure: String?
)
