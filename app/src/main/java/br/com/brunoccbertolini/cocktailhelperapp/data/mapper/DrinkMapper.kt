package br.com.brunoccbertolini.cocktailhelperapp.data.mapper

import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.CachedDrinkEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.local.entity.DrinkPreviewEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.DrinkDetailDto
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.DrinkSummaryDto
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.Ingredient

fun DrinkSummaryDto.toDomain() = DrinkSummary(
    id = idDrink,
    name = strDrink,
    thumbnailUrl = strDrinkThumb
)

fun DrinkPreviewEntity.toDomain() = DrinkSummary(
    id = idDrink,
    name = strDrink,
    thumbnailUrl = strDrinkThumb
)

fun CachedDrinkEntity.toDomain() = DrinkSummary(
    id = idDrink,
    name = strDrink,
    thumbnailUrl = strDrinkThumb
)

fun DrinkSummary.toEntity() = DrinkPreviewEntity(
    idDrink = id,
    strDrink = name,
    strDrinkThumb = thumbnailUrl
)

fun DrinkSummary.toCacheEntity(type: String) = CachedDrinkEntity(
    idDrink = id,
    strDrink = name,
    strDrinkThumb = thumbnailUrl,
    drinkType = type
)

fun DrinkSummaryDto.toCacheEntity(type: String) = CachedDrinkEntity(
    idDrink = idDrink,
    strDrink = strDrink,
    strDrinkThumb = strDrinkThumb,
    drinkType = type
)

fun DrinkDetailDto.toDomain(): DrinkDetail {
    val ingredientNames = listOf(
        strIngredient1, strIngredient2, strIngredient3, strIngredient4, strIngredient5,
        strIngredient6, strIngredient7, strIngredient8, strIngredient9, strIngredient10,
        strIngredient11, strIngredient12, strIngredient13, strIngredient14, strIngredient15
    )
    val measures = listOf(
        strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5,
        strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10,
        strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15
    )
    val ingredients = ingredientNames.mapIndexedNotNull { index, name ->
        if (!name.isNullOrBlank()) Ingredient(name = name, measure = measures.getOrNull(index))
        else null
    }
    return DrinkDetail(
        id = idDrink.orEmpty(),
        name = strDrink.orEmpty(),
        category = strCategory,
        alcoholic = strAlcoholic,
        glass = strGlass,
        instructions = strInstructions,
        thumbnailUrl = strDrinkThumb,
        ingredients = ingredients
    )
}
