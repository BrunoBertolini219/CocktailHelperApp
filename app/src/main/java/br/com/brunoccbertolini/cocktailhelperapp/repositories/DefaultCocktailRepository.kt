package br.com.brunoccbertolini.cocktailhelperapp.repositories

import androidx.lifecycle.LiveData
import br.com.brunoccbertolini.cocktailhelperapp.api.CocktailAPI
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import retrofit2.Response
import javax.inject.Inject


class DefaultCocktailRepository @Inject constructor(
    private val cocktailDao: CocktailDao,
    private val cocktailApi: CocktailAPI
) : CocktailRepository {
    override suspend fun getAllAlcoholicDrinks(): Resource<CocktailList> {
        return handleCocktailListResponse(cocktailApi.getAllAlcoholicDrinks())
    }

    override suspend fun getAllNoAlcoholicDrinks(): Resource<CocktailList> {
        return handleCocktailListResponse(cocktailApi.getAllNoAlcoholicDrinks())
    }

    override suspend fun searchDrinkById(searchDrinkId: String): Resource<DrinkList> {
        return handleDrinkListResponse(cocktailApi.searchDrinksById(searchDrinkId))
    }

    override suspend fun randomDrink(): Resource<DrinkList> {
        return handleDrinkListResponse(cocktailApi.getRandomDrink())
    }

    override suspend fun deleteCocktail(drink: DrinkPreview) {
        cocktailDao.deleteCocktail(drink)
    }

    override fun getSavedCocktails(): LiveData<List<DrinkPreview>> {
        return cocktailDao.getAllCocktail()
    }

    override suspend fun upsert(drink: DrinkPreview) {
        cocktailDao.upsert(drink)
    }

    override suspend fun searchDrinkByName(searchDrinkName: String): Resource<CocktailList> {
        return handleCocktailListResponse(cocktailApi.searchDrinksByName(searchDrinkName))
    }

    override suspend fun searchDrinkByIngredient(searchDrinkIngredient: String): Resource<CocktailList> {
        return handleCocktailListResponse(cocktailApi.searchDrinksByIngredient(searchDrinkIngredient))
    }

    private fun handleCocktailListResponse(response: Response<CocktailList>): Resource<CocktailList> {
        return try {
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?: Resource.Error("An unknown error has ocrrured")
            } else {
                return Resource.Error("An unknown error has ocurred")
            }
        } catch (e: Exception) {
            return Resource.Error(
                "We couldn't reach the server. Check your internet connection",
                null
            )
        }
    }

    private fun handleDrinkListResponse(response: Response<DrinkList>): Resource<DrinkList> {
        return try {
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?: Resource.Error("An unknown error has ocrrured")
            } else {
                return Resource.Error("An unknown error has ocurred")
            }
        } catch (e: Exception) {
            return Resource.Error(
                "We couldn't reach the server. Check your internet connection",
                null
            )
        }
    }


}
