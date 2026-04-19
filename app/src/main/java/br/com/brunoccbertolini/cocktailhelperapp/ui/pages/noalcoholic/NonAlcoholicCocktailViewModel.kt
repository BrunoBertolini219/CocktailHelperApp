package br.com.brunoccbertolini.cocktailhelperapp.ui.pages.noalcoholic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NonAlcoholicCocktailViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
) : ViewModel() {

    private val _networkError = MutableStateFlow<String?>(null)

    val cocktailNoAlcoholic: StateFlow<Resource<CocktailList>> = combine(
        cocktailRepository.getCachedNonAlcoholicDrinks(), _networkError
    ) { cached, error ->
        when {
            cached.isNotEmpty() -> Resource.Success(CocktailList(cached))
            error != null -> Resource.Error(error)
            else -> Resource.Loading()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading())

    init {
        refresh()
    }

    fun refresh() = viewModelScope.launch {
        _networkError.value = null
        val result = cocktailRepository.getAllNoAlcoholicDrinks()
        if (result is Resource.Error) _networkError.value = result.message
    }
}
