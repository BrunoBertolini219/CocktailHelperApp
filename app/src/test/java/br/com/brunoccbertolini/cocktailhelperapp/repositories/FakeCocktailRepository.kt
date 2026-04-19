package br.com.brunoccbertolini.cocktailhelperapp.repositories

import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeCocktailRepository : CocktailRepository {

    private val cocktailsList = mutableListOf<DrinkPreview>()
    private val observableDrinkPreview = MutableStateFlow<List<DrinkPreview>>(emptyList())

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshFlow() {
        observableDrinkPreview.value = cocktailsList.toList()
    }

    override suspend fun getAllAlcoholicDrinks(): Resource<CocktailList> = checkResourceCocktailWorks()

    override suspend fun getAllNoAlcoholicDrinks(): Resource<CocktailList> = checkResourceCocktailWorks()

    override suspend fun searchDrinkById(searchDrinkId: String): Resource<DrinkList> = checkResourceDrinkWorks()

    override suspend fun randomDrink(): Resource<DrinkList> = checkResourceDrinkWorks()

    override suspend fun searchDrinkByName(searchDrinkName: String): Resource<CocktailList> = checkResourceCocktailWorks()

    override suspend fun searchDrinkByIngredient(searchDrinkIngredient: String): Resource<CocktailList> = checkResourceCocktailWorks()

    override suspend fun deleteCocktail(drink: DrinkPreview) {
        cocktailsList.remove(drink)
        refreshFlow()
    }

    override fun getSavedCocktails(): Flow<List<DrinkPreview>> = observableDrinkPreview.asStateFlow()

    override suspend fun upsert(drink: DrinkPreview) {
        if (drink.strDrink.isEmpty() || drink.strDrinkThumb.isNullOrEmpty()) {
            Resource.Error("ERROR", null)
        } else {
            cocktailsList.add(drink)
            refreshFlow()
        }
    }

    private fun checkResourceCocktailWorks(): Resource<CocktailList> {
        return if (shouldReturnNetworkError) {
            Resource.Error("Error", null)
        } else {
            Resource.Success(CocktailList(listOf()))
        }
    }

    private fun checkResourceDrinkWorks(): Resource<DrinkList> {
        return if (shouldReturnNetworkError) {
            Resource.Error("Error", null)
        } else {
            Resource.Success(DrinkList(listOf()))
        }
    }
}
