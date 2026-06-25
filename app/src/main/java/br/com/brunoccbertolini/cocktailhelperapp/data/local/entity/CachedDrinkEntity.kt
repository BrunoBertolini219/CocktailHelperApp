package br.com.brunoccbertolini.cocktailhelperapp.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cached_drinks",
    indices = [Index("drinkType")]
)
data class CachedDrinkEntity(
    @PrimaryKey val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String?,
    val drinkType: String
)
