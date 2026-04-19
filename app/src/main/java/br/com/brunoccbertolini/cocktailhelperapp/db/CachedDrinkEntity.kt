package br.com.brunoccbertolini.cocktailhelperapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview

@Entity(tableName = "cached_drinks")
data class CachedDrinkEntity(
    @PrimaryKey val idDrink: String,
    val strDrink: String,
    val strDrinkThumb: String?,
    val drinkType: String
)

fun CachedDrinkEntity.toDrinkPreview() = DrinkPreview(idDrink, strDrink, strDrinkThumb)
fun DrinkPreview.toCacheEntity(type: String) = CachedDrinkEntity(idDrink, strDrink, strDrinkThumb, type)
