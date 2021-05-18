package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.searchName
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response


class SearchCocktailViewModel(val repository: CocktailRepository): ViewModel() {
    var searchCocktail: MutableLiveData<Resource<CocktailList>> = MutableLiveData()


    init {

    }

    fun searchCocktail(searchQuery: String, searchType: String) = viewModelScope.launch {
        searchCocktail.postValue(Resource.Loading())
        val response = if (searchType == searchName) {
            repository.searchDrinkByName(searchQuery)
        } else {
            repository.searchDrinkByIngredient(searchQuery)
        }
        searchCocktail.postValue(handleCocktailResponse(response))
    }

    private fun handleCocktailResponse(response: Response<CocktailList>): Resource<CocktailList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
