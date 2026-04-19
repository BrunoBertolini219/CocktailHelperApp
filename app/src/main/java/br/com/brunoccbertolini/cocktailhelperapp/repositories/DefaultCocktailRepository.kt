package br.com.brunoccbertolini.cocktailhelperapp.repositories

import br.com.brunoccbertolini.cocktailhelperapp.api.CocktailAPI
import br.com.brunoccbertolini.cocktailhelperapp.db.CachedDrinkEntity
import br.com.brunoccbertolini.cocktailhelperapp.db.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.db.toCacheEntity
import br.com.brunoccbertolini.cocktailhelperapp.db.toDrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class DefaultCocktailRepository @Inject constructor(
    private val cocktailDao: CocktailDao,
    private val cocktailApi: CocktailAPI
) : CocktailRepository {

    companion object {
        const val ALCOHOLIC = "alcoholic"
        const val NON_ALCOHOLIC = "non_alcoholic"
    }

    override suspend fun getAllAlcoholicDrinks(): Resource<CocktailList> {
        val result = handleCocktailListResponse(cocktailApi.getAllAlcoholicDrinks())
        if (result is Resource.Success) {
            val drinks = result.data?.drinks ?: emptyList()
            cocktailDao.clearCachedDrinks(ALCOHOLIC)
            cocktailDao.insertCachedDrinks(drinks.map { it.toCacheEntity(ALCOHOLIC) })
        }
        return result
    }

    override suspend fun getAllNoAlcoholicDrinks(): Resource<CocktailList> {
        val result = handleCocktailListResponse(cocktailApi.getAllNoAlcoholicDrinks())
        if (result is Resource.Success) {
            val drinks = result.data?.drinks ?: emptyList()
            cocktailDao.clearCachedDrinks(NON_ALCOHOLIC)
            cocktailDao.insertCachedDrinks(drinks.map { it.toCacheEntity(NON_ALCOHOLIC) })
        }
        return result
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

    override fun getSavedCocktails(): Flow<List<DrinkPreview>> {
        return cocktailDao.getAllCocktail()
    }

    override fun getCachedAlcoholicDrinks(): Flow<List<DrinkPreview>> {
        return cocktailDao.getCachedDrinks(ALCOHOLIC).map { list -> list.map { it.toDrinkPreview() } }
    }

    override fun getCachedNonAlcoholicDrinks(): Flow<List<DrinkPreview>> {
        return cocktailDao.getCachedDrinks(NON_ALCOHOLIC).map { list -> list.map { it.toDrinkPreview() } }
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
                } ?: Resource.Error("An unknown error has occurred")
            } else {
                Resource.Error("An unknown error has occurred")
            }
        } catch (e: Exception) {
            Resource.Error("We couldn't reach the server. Check your internet connection", null)
        }
    }

    private fun handleDrinkListResponse(response: Response<DrinkList>): Resource<DrinkList> {
        return try {
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.Success(it)
                } ?: Resource.Error("An unknown error has occurred")
            } else {
                Resource.Error("An unknown error has occurred")
            }
        } catch (e: Exception) {
            Resource.Error("We couldn't reach the server. Check your internet connection", null)
        }
    }
}
