package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class AlcoholicCocktailViewModel(
    val cockTailRepository: CocktailRepository
) : ViewModel() {
    val cocktailAlcoholic: MutableLiveData<Resource<CocktailList>> = MutableLiveData()

    init {

    }
    fun getAlcoholicCocktails() = viewModelScope.launch {
            cocktailAlcoholic.postValue(Resource.Loading())
            val response = cockTailRepository.getAllAlcoholicDrinks()
            cocktailAlcoholic.postValue(handleAlcoholicCocktailResponse(response))
        }



    private fun handleAlcoholicCocktailResponse(response: Response<CocktailList>): Resource<CocktailList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}
