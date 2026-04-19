package br.com.brunoccbertolini.cocktailhelperapp.repositories

import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {

    suspend fun getAllAlcoholicDrinks(): Resource<CocktailList>

    suspend fun getAllNoAlcoholicDrinks(): Resource<CocktailList>

    suspend fun searchDrinkById(searchDrinkId: String): Resource<DrinkList>

    suspend fun randomDrink(): Resource<DrinkList>

    suspend fun deleteCocktail(drink: DrinkPreview)

    fun getSavedCocktails(): Flow<List<DrinkPreview>>

    suspend fun upsert(drink: DrinkPreview)

    suspend fun searchDrinkByName(searchDrinkName: String): Resource<CocktailList>

    suspend fun searchDrinkByIngredient(searchDrinkIngredient: String): Resource<CocktailList>
}
