package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

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
class AlcoholicCocktailViewModel @Inject constructor(
    private val cockTailRepository: CocktailRepository
) : ViewModel() {
    private val _cocktailAlcoholic = MutableStateFlow<Resource<CocktailList>>(Resource.Loading())
    val cocktailAlcoholic: StateFlow<Resource<CocktailList>> = _cocktailAlcoholic.asStateFlow()

    fun getAlcoholicCocktails() = viewModelScope.launch {
        _cocktailAlcoholic.value = Resource.Loading()
        _cocktailAlcoholic.value = cockTailRepository.getAllAlcoholicDrinks()
    }
}
