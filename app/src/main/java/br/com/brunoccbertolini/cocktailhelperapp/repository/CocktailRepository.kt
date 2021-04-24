package br.com.brunoccbertolini.cocktailhelperapp.repository

import br.com.brunoccbertolini.cocktailhelperapp.api.RetrofitInstance
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDatabase
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview

class CocktailRepository(val db: CocktailDatabase) {

    suspend fun getAllAlcoholicDrinks() = RetrofitInstance.api.getAllAlcoholicDrinks()

    suspend fun getAllNoAlcoholicDrinks() = RetrofitInstance.api.getAllNoAlcoholicDrinks()

    suspend fun searchDrinkById(searchDrinkId:String) = RetrofitInstance.api.searchDrinksById(searchDrinkId)

    suspend fun upsert(drink: DrinkPreview) = db.getCocktailDao().upsert(drink)

    fun getSavedCocktails() = db.getCocktailDao().getAllCocktail()

    suspend fun searchDrinkByName(searchDrinkName: String) = RetrofitInstance.api.searchDrinksByName(searchDrinkName)

    suspend fun deleteCocktail(drink: DrinkPreview) = db.getCocktailDao().deleteCocktail(drink)

}