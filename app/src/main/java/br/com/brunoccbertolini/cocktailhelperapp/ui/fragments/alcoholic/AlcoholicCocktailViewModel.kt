package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AlcoholicCocktailViewModel @Inject constructor(
    private val cockTailRepository: CocktailRepository
) : ViewModel() {
    val cocktailAlcoholic: MutableLiveData<Resource<CocktailList>> = MutableLiveData()

    fun getAlcoholicCocktails() = viewModelScope.launch {
            cocktailAlcoholic.postValue(Resource.Loading())
            val response = cockTailRepository.getAllAlcoholicDrinks()
            cocktailAlcoholic.postValue(response)
        }


}
