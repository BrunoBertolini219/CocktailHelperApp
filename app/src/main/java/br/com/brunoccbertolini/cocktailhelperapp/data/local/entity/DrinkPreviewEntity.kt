package br.com.brunoccbertolini.cocktailhelperapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cocktails")
data class DrinkPreviewEntity(
    @PrimaryKey(autoGenerate = false)
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String?
)
