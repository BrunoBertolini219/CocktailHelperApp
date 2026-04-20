package br.com.brunoccbertolini.cocktailhelperapp.domain.repository

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {
    fun getAlcoholicDrinks(): Flow<Resource<List<DrinkSummary>>>
    fun getNonAlcoholicDrinks(): Flow<Resource<List<DrinkSummary>>>
    suspend fun getDrinkDetail(id: String): Resource<DrinkDetail>
    suspend fun searchByName(query: String): Resource<List<DrinkSummary>>
    suspend fun searchByIngredient(query: String): Resource<List<DrinkSummary>>
    suspend fun getRandomDrink(): Resource<DrinkDetail>
    suspend fun saveFavorite(drink: DrinkSummary)
    fun getFavorites(): Flow<List<DrinkSummary>>
    suspend fun deleteFavorite(drink: DrinkSummary)
}
