package br.com.brunoccbertolini.cocktailhelperapp.repositories

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkDetail
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class FakeCocktailRepository : CocktailRepository {

    private val favoritesList = mutableListOf<DrinkSummary>()
    private val observableFavorites = MutableStateFlow<List<DrinkSummary>>(emptyList())

    var shouldReturnNetworkError = false

    private fun refreshFavorites() {
        observableFavorites.value = favoritesList.toList()
    }

    override fun getAlcoholicDrinks(): Flow<Resource<List<DrinkSummary>>> = flow {
        emit(
            if (shouldReturnNetworkError) Resource.Error("Error")
            else Resource.Success(emptyList())
        )
    }

    override fun getNonAlcoholicDrinks(): Flow<Resource<List<DrinkSummary>>> = flow {
        emit(
            if (shouldReturnNetworkError) Resource.Error("Error")
            else Resource.Success(emptyList())
        )
    }

    override suspend fun getDrinkDetail(id: String): Resource<DrinkDetail> =
        if (shouldReturnNetworkError) Resource.Error("Error")
        else Resource.Success(DrinkDetail(id, "Test", null, null, null, null, null, emptyList()))

    override suspend fun searchByName(query: String): Resource<List<DrinkSummary>> =
        if (shouldReturnNetworkError) Resource.Error("Error") else Resource.Success(emptyList())

    override suspend fun searchByIngredient(query: String): Resource<List<DrinkSummary>> =
        if (shouldReturnNetworkError) Resource.Error("Error") else Resource.Success(emptyList())

    override suspend fun getRandomDrink(): Resource<DrinkDetail> =
        if (shouldReturnNetworkError) Resource.Error("Error")
        else Resource.Success(DrinkDetail("1", "Random", null, null, null, null, null, emptyList()))

    override suspend fun saveFavorite(drink: DrinkSummary) {
        favoritesList.add(drink)
        refreshFavorites()
    }

    override fun getFavorites(): Flow<List<DrinkSummary>> = observableFavorites.asStateFlow()

    override suspend fun deleteFavorite(drink: DrinkSummary) {
        favoritesList.remove(drink)
        refreshFavorites()
    }
}
