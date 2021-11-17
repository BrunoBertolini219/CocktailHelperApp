package br.com.brunoccbertolini.cocktailhelperapp.repositories

import androidx.lifecycle.LiveData
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource

interface CocktailRepository {

    suspend fun getAllAlcoholicDrinks(): Resource<CocktailList>

    suspend fun getAllNoAlcoholicDrinks(): Resource<CocktailList>

    suspend fun searchDrinkById(searchDrinkId: String): Resource<DrinkList>

    suspend fun randomDrink():Resource<DrinkList>

    suspend fun deleteCocktail(drink: DrinkPreview)

    fun getSavedCocktails(): LiveData<List<DrinkPreview>>

    suspend fun upsert(drink: DrinkPreview)

    suspend fun searchDrinkByName(searchDrinkName: String): Resource<CocktailList>

    suspend fun searchDrinkByIngredient(searchDrinkIngredient: String): Resource<CocktailList>

}