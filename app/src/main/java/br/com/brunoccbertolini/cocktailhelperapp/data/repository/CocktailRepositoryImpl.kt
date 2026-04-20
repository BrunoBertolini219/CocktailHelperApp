package br.com.brunoccbertolini.cocktailhelperapp.data.repository

import br.com.brunoccbertolini.cocktailhelperapp.data.local.dao.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.data.mapper.toCacheEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.mapper.toDomain
import br.com.brunoccbertolini.cocktailhelperapp.data.mapper.toEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.api.CocktailApi
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.CocktailListDto
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.DrinkListDto
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class CocktailRepositoryImpl @Inject constructor(
    private val dao: CocktailDao,
    private val api: CocktailApi
) : CocktailRepository {

    companion object {
        private const val ALCOHOLIC = "alcoholic"
        private const val NON_ALCOHOLIC = "non_alcoholic"
    }

    override fun getAlcoholicDrinks(): Flow<Resource<List<DrinkSummary>>> =
        cachedDrinksFlow(ALCOHOLIC) { api.getAllAlcoholicDrinks() }

    override fun getNonAlcoholicDrinks(): Flow<Resource<List<DrinkSummary>>> =
        cachedDrinksFlow(NON_ALCOHOLIC) { api.getAllNoAlcoholicDrinks() }

    private fun cachedDrinksFlow(
        type: String,
        networkCall: suspend () -> Response<CocktailListDto>
    ): Flow<Resource<List<DrinkSummary>>> = flow {
        emit(Resource.Loading())
        val cached = dao.getCachedDrinks(type).first()
        if (cached.isNotEmpty()) {
            emit(Resource.Success(cached.map { it.toDomain() }))
        }
        try {
            val response = networkCall()
            if (response.isSuccessful) {
                val drinks = response.body()?.drinks ?: emptyList()
                dao.clearCachedDrinks(type)
                dao.insertCachedDrinks(drinks.map { it.toCacheEntity(type) })
                emit(Resource.Success(drinks.map { it.toDomain() }))
            } else {
                val fallback = if (cached.isNotEmpty()) cached.map { it.toDomain() } else null
                emit(Resource.Error("An unknown error has occurred", fallback))
            }
        } catch (e: Exception) {
            val fallback = if (cached.isNotEmpty()) cached.map { it.toDomain() } else null
            emit(Resource.Error("We couldn't reach the server. Check your internet connection", fallback))
        }
    }

    override suspend fun getDrinkDetail(id: String): Resource<DrinkDetail> =
        handleDrinkListResponse(api.searchDrinksById(id))

    override suspend fun searchByName(query: String): Resource<List<DrinkSummary>> =
        handleCocktailListResponse(api.searchDrinksByName(query))

    override suspend fun searchByIngredient(query: String): Resource<List<DrinkSummary>> =
        handleCocktailListResponse(api.searchDrinksByIngredient(query))

    override suspend fun getRandomDrink(): Resource<DrinkDetail> =
        handleDrinkListResponse(api.getRandomDrink())

    override suspend fun saveFavorite(drink: DrinkSummary) {
        dao.upsert(drink.toEntity())
    }

    override fun getFavorites(): Flow<List<DrinkSummary>> =
        dao.getAllCocktails().map { list -> list.map { it.toDomain() } }

    override suspend fun deleteFavorite(drink: DrinkSummary) {
        dao.deleteCocktail(drink.toEntity())
    }

    private fun handleCocktailListResponse(response: Response<CocktailListDto>): Resource<List<DrinkSummary>> =
        try {
            if (response.isSuccessful) {
                Resource.Success(response.body()?.drinks?.map { it.toDomain() } ?: emptyList())
            } else {
                Resource.Error("An unknown error has occurred")
            }
        } catch (e: Exception) {
            Resource.Error("We couldn't reach the server. Check your internet connection")
        }

    private fun handleDrinkListResponse(response: Response<DrinkListDto>): Resource<DrinkDetail> =
        try {
            if (response.isSuccessful) {
                val drink = response.body()?.drinks?.firstOrNull()
                if (drink != null) Resource.Success(drink.toDomain())
                else Resource.Error("Drink not found")
            } else {
                Resource.Error("An unknown error has occurred")
            }
        } catch (e: Exception) {
            Resource.Error("We couldn't reach the server. Check your internet connection")
        }
}
