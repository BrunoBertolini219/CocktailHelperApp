package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.searchName
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class SearchCocktailViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel() {
    var searchCocktail: MutableLiveData<Resource<CocktailList>> = MutableLiveData()

    fun searchCocktail(searchQuery: String, searchType: String) = viewModelScope.launch {
        searchCocktail.postValue(Resource.Loading())
        val response = if (searchType == searchName) {
            repository.searchDrinkByName(searchQuery)
        } else {
            repository.searchDrinkByIngredient(searchQuery)
        }
        searchCocktail.postValue(response)
    }
}
