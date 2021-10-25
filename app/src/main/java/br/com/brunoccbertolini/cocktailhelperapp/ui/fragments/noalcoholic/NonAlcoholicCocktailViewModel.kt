package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.noalcoholic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NonAlcoholicCocktailViewModel @Inject constructor(
    private val cocktailRepository: CocktailRepository
) : ViewModel() {

    private var _cocktailNoAlcoholic = MutableLiveData<Resource<CocktailList>>()
    val cocktailNoAlcoholic: MutableLiveData<Resource<CocktailList>> get() = _cocktailNoAlcoholic

    fun getNonAlcoholicCocktails() = viewModelScope.launch {
            cocktailNoAlcoholic.postValue(Resource.Loading())
            val response = cocktailRepository.getAllNoAlcoholicDrinks()
            cocktailNoAlcoholic.postValue(response)
    }
}