package br.com.brunoccbertolini.cocktailhelperapp.ui.pages.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Constrants.Companion.searchName
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchCocktailViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel() {
    private val _searchCocktail = MutableStateFlow<Resource<CocktailList>?>(null)
    val searchCocktail: StateFlow<Resource<CocktailList>?> = _searchCocktail.asStateFlow()

    fun searchCocktail(searchQuery: String, searchType: String) = viewModelScope.launch {
        _searchCocktail.value = Resource.Loading()
        _searchCocktail.value = if (searchType == searchName) {
            repository.searchDrinkByName(searchQuery)
        } else {
            repository.searchDrinkByIngredient(searchQuery)
        }
    }
}
