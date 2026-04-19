package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.noalcoholic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NonAlcoholicCocktailViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
) : ViewModel() {
    private val _cocktailNoAlcoholic = MutableStateFlow<Resource<CocktailList>>(Resource.Loading())
    val cocktailNoAlcoholic: StateFlow<Resource<CocktailList>> = _cocktailNoAlcoholic.asStateFlow()

    fun getNonAlcoholicCocktails() = viewModelScope.launch {
        _cocktailNoAlcoholic.value = Resource.Loading()
        _cocktailNoAlcoholic.value = cocktailRepository.getAllNoAlcoholicDrinks()
    }
}
