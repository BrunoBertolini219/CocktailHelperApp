package br.com.brunoccbertolini.cocktailhelperapp.model

import androidx.room.Entity


data class CocktailList(
    val drinks: List<DrinkPreview>
)