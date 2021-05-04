package br.com.brunoccbertolini.cocktailhelperapp.repository

import br.com.brunoccbertolini.cocktailhelperapp.api.RetrofitInstance
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview

class CocktailRepository(val db: CocktailDatabase) {

    suspend fun getAllAlcoholicDrinks() = RetrofitInstance.api.getAllAlcoholicDrinks()

    suspend fun getAllNoAlcoholicDrinks() = RetrofitInstance.api.getAllNoAlcoholicDrinks()

    suspend fun searchDrinkById(searchDrinkId: String) =
        RetrofitInstance.api.searchDrinksById(searchDrinkId)

    suspend fun randomDrink() = RetrofitInstance.api.getRandomDrink()

    suspend fun deleteCocktail(drink: DrinkPreview) = db.getCocktailDao().deleteCocktail(drink)

    fun getSavedCocktails() = db.getCocktailDao().getAllCocktail()

    suspend fun upsert(drink: DrinkPreview) = db.getCocktailDao().upsert(drink)

    suspend fun searchDrinkByName(searchDrinkName: String) =
        RetrofitInstance.api.searchDrinksByName(searchDrinkName)

    suspend fun searchDrinkByIngredient(searchDrinkIngredient: String) =
        RetrofitInstance.api.searchDrinksByIngredient(searchDrinkIngredient)
}