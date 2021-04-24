package br.com.brunoccbertolini.cocktailhelperapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "cocktails"
)
data class DrinkPreview(
    @PrimaryKey(autoGenerate = false)
    val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String?
): Serializable