package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class DetailViewModel(
    val repository: CocktailRepository
): ViewModel() {
    val drinkLiveData = MutableLiveData<Resource<DrinkList>>()

    fun getDrinkDetail(id: String)  = viewModelScope.launch {
        drinkLiveData.postValue(Resource.Loading())
        val response = repository.searchDrinkById(id)
        drinkLiveData.postValue(handleSearchCocktailResponse(response))
    }

    private fun handleSearchCocktailResponse(response: Response<DrinkList>):Resource<DrinkList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveCocktail(drink: DrinkPreview) = viewModelScope.launch {
        repository.upsert(drink)
    }

    fun getRandomDrink() = viewModelScope.launch {
        drinkLiveData.postValue(Resource.Loading())
        val response = repository.randomDrink()
        drinkLiveData.postValue(handleSearchCocktailResponse(response))
    }

}
