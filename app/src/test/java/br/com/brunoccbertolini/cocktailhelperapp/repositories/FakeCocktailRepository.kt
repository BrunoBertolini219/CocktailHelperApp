package br.com.brunoccbertolini.cocktailhelperapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource

class FakeCocktailRepository : CocktailRepository {

    private val cocktailsList = mutableListOf<DrinkPreview>()

    private val observableDrinkPreview = MutableLiveData<List<DrinkPreview>>(cocktailsList)

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableDrinkPreview.postValue(cocktailsList)
    }

    override suspend fun getAllAlcoholicDrinks(): Resource<CocktailList> {
        return checkResourceCocktailWorks()
    }

    override suspend fun getAllNoAlcoholicDrinks(): Resource<CocktailList> {
        return checkResourceCocktailWorks()
    }

    override suspend fun searchDrinkById(searchDrinkId: String): Resource<DrinkList> {
        return checkResourceDrinkWorks()
    }

    override suspend fun randomDrink(): Resource<DrinkList> {
        return checkResourceDrinkWorks()
    }

    override suspend fun searchDrinkByName(searchDrinkName: String): Resource<CocktailList> {
        return checkResourceCocktailWorks()
    }

    override suspend fun searchDrinkByIngredient(searchDrinkIngredient: String): Resource<CocktailList> {
        return checkResourceCocktailWorks()
    }

    override suspend fun deleteCocktail(drink: DrinkPreview) {
        cocktailsList.remove(drink)
        refreshLiveData()
    }

    override fun getSavedCocktails(): LiveData<List<DrinkPreview>> {
        return observableDrinkPreview
    }

    override suspend fun upsert(drink: DrinkPreview) {
        if(drink.strDrink.isEmpty() || drink.strDrinkThumb.isNullOrEmpty()){
            Resource.Error("ERROR", null)
        }else {
            cocktailsList.add(drink)
            refreshLiveData()
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
