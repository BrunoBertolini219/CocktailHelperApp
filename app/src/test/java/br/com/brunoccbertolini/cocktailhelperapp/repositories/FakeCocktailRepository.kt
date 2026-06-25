package br.com.brunoccbertolini.cocktailhelperapp.repositories

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.DataError
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

/** In-memory fake used by ViewModel unit tests. Toggle [shouldReturnNetworkError] to fail calls. */
class FakeCocktailRepository : CocktailRepository {

    var shouldReturnNetworkError = false
    var alcoholicDrinks = listOf(DrinkSummary("11007", "Margarita", null))
    var nonAlcoholicDrinks = listOf(DrinkSummary("12572", "Lemonade", null))
    var searchResult = listOf(DrinkSummary("11000", "Mojito", null))
    var randomDrink = DrinkDetail("99", "Random", null, null, null, null, null, emptyList())
    var detailDrink = DrinkDetail("1", "Mojito", "Cocktail", "Alcoholic", "Highball glass", "Mix", null, emptyList())

    private val favorites = mutableListOf<DrinkSummary>()
    private val observableFavorites = MutableStateFlow<List<DrinkSummary>>(emptyList())

    private val error = DataError.Network.NO_INTERNET

    override fun getAlcoholicDrinks(): Flow<Resource<List<DrinkSummary>>> = flow {
        emit(Resource.Loading)
        emit(if (shouldReturnNetworkError) Resource.Error(error) else Resource.Success(alcoholicDrinks))
    }

    override fun getNonAlcoholicDrinks(): Flow<Resource<List<DrinkSummary>>> = flow {
        emit(Resource.Loading)
        emit(if (shouldReturnNetworkError) Resource.Error(error) else Resource.Success(nonAlcoholicDrinks))
    }

    override suspend fun getDrinkDetail(id: String): Resource<DrinkDetail> =
        if (shouldReturnNetworkError) Resource.Error(error) else Resource.Success(detailDrink)

    override suspend fun searchByName(query: String): Resource<List<DrinkSummary>> =
        if (shouldReturnNetworkError) Resource.Error(error) else Resource.Success(searchResult)

    override suspend fun searchByIngredient(query: String): Resource<List<DrinkSummary>> =
        if (shouldReturnNetworkError) Resource.Error(error) else Resource.Success(searchResult)

    override suspend fun getRandomDrink(): Resource<DrinkDetail> =
        if (shouldReturnNetworkError) Resource.Error(error) else Resource.Success(randomDrink)

    override suspend fun saveFavorite(drink: DrinkSummary) {
        favorites.add(drink)
        observableFavorites.value = favorites.toList()
    }

    override fun getFavorites(): Flow<List<DrinkSummary>> = observableFavorites.asStateFlow()

    override suspend fun deleteFavorite(drink: DrinkSummary) {
        favorites.remove(drink)
        observableFavorites.value = favorites.toList()
    }
}
