package br.com.brunoccbertolini.cocktailhelperapp.data.repository

import br.com.brunoccbertolini.cocktailhelperapp.data.local.dao.CocktailDao
import br.com.brunoccbertolini.cocktailhelperapp.data.mapper.toCacheEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.mapper.toDetailEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.mapper.toDomain
import br.com.brunoccbertolini.cocktailhelperapp.data.mapper.toEntity
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.api.CocktailApi
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.CocktailListDto
import br.com.brunoccbertolini.cocktailhelperapp.data.remote.dto.DrinkListDto
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.DataError
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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

    /**
     * Offline-first: emit cached drinks immediately (if any), then refresh from the network.
     * On failure, surface a typed [DataError] while keeping the cached list as fallback data.
     */
    private fun cachedDrinksFlow(
        type: String,
        networkCall: suspend () -> Response<CocktailListDto>
    ): Flow<Resource<List<DrinkSummary>>> = flow {
        emit(Resource.Loading)
        val cached = dao.getCachedDrinks(type).first()
        val fallback = if (cached.isNotEmpty()) cached.map { it.toDomain() } else null
        fallback?.let { emit(Resource.Success(it)) }
        try {
            val response = networkCall()
            if (response.isSuccessful) {
                val drinks = response.body()?.drinks.orEmpty()
                dao.clearCachedDrinks(type)
                dao.insertCachedDrinks(drinks.map { it.toCacheEntity(type) })
                emit(Resource.Success(drinks.map { it.toDomain() }))
            } else {
                emit(Resource.Error(response.code().toNetworkError(), fallback))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toDataError(), fallback))
        }
    }

    /**
     * Offline-first: fetch the detail from the network and cache it; if the network fails,
     * fall back to the cached copy (so favorites and recently-viewed drinks open offline).
     */
    override suspend fun getDrinkDetail(id: String): Resource<DrinkDetail> =
        try {
            val response = api.searchDrinksById(id)
            if (response.isSuccessful) {
                val drink = response.body()?.drinks?.firstOrNull()?.toDomain()
                if (drink != null) {
                    dao.upsertDetail(drink.toDetailEntity())
                    Resource.Success(drink)
                } else {
                    cachedDetailOr(id, DataError.Network.NOT_FOUND)
                }
            } else {
                cachedDetailOr(id, response.code().toNetworkError())
            }
        } catch (e: Exception) {
            cachedDetailOr(id, e.toDataError())
        }

    private suspend fun cachedDetailOr(id: String, error: DataError): Resource<DrinkDetail> =
        dao.getCachedDetail(id)?.toDomain()?.let { Resource.Success(it) } ?: Resource.Error(error)

    override suspend fun searchByName(query: String): Resource<List<DrinkSummary>> =
        drinkSummariesOf { api.searchDrinksByName(query) }

    override suspend fun searchByIngredient(query: String): Resource<List<DrinkSummary>> =
        drinkSummariesOf { api.searchDrinksByIngredient(query) }

    override suspend fun getRandomDrink(): Resource<DrinkDetail> =
        drinkDetailOf { api.getRandomDrink() }

    override suspend fun saveFavorite(drink: DrinkSummary) = dao.upsert(drink.toEntity())

    override fun getFavorites(): Flow<List<DrinkSummary>> =
        dao.getAllFavorites().map { list -> list.map { it.toDomain() } }

    override suspend fun deleteFavorite(drink: DrinkSummary) = dao.deleteFavorite(drink.toEntity())

    private suspend fun drinkSummariesOf(
        call: suspend () -> Response<CocktailListDto>
    ): Resource<List<DrinkSummary>> = safeCall {
        val response = call()
        if (response.isSuccessful) {
            Resource.Success(response.body()?.drinks?.map { it.toDomain() }.orEmpty())
        } else {
            Resource.Error(response.code().toNetworkError())
        }
    }

    private suspend fun drinkDetailOf(
        call: suspend () -> Response<DrinkListDto>
    ): Resource<DrinkDetail> = safeCall {
        val response = call()
        if (response.isSuccessful) {
            response.body()?.drinks?.firstOrNull()
                ?.let { Resource.Success(it.toDomain()) }
                ?: Resource.Error(DataError.Network.NOT_FOUND)
        } else {
            Resource.Error(response.code().toNetworkError())
        }
    }

    /** Runs a network block and converts any thrown exception into a typed error. */
    private inline fun <T> safeCall(block: () -> Resource<T>): Resource<T> =
        try {
            block()
        } catch (e: Exception) {
            Resource.Error(e.toDataError())
        }
}

private fun Int.toNetworkError(): DataError = when (this) {
    404 -> DataError.Network.NOT_FOUND
    in 500..599 -> DataError.Network.SERVER
    else -> DataError.Network.UNKNOWN
}

private fun Throwable.toDataError(): DataError = when (this) {
    is SocketTimeoutException -> DataError.Network.TIMEOUT
    is UnknownHostException, is IOException -> DataError.Network.NO_INTERNET
    is SerializationException -> DataError.Network.SERIALIZATION
    else -> DataError.Network.UNKNOWN
}
