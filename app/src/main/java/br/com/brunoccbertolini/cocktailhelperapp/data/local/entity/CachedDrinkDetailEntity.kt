package br.com.brunoccbertolini.cocktailhelperapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.Ingredient

/** Full drink detail cached for offline access (favorites & recently viewed). */
@Entity(tableName = "cached_drink_details")
data class CachedDrinkDetailEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String?,
    val alcoholic: String?,
    val glass: String?,
    val instructions: String?,
    val thumbnailUrl: String?,
    val ingredients: List<Ingredient>
)
