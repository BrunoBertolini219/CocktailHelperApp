package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.noalcoholic

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.CocktailList
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class NonAlcoholicCocktailViewModel(val cocktailRepository: CocktailRepository) : ViewModel() {

    private var _cocktailNoAlcoholic = MutableLiveData<Resource<CocktailList>>()
    val cocktailNoAlcoholic: MutableLiveData<Resource<CocktailList>> get() = _cocktailNoAlcoholic

    init {
        getNonAlcoholicCocktails()
    }

    fun getNonAlcoholicCocktails() = viewModelScope.launch {
        try {


            cocktailNoAlcoholic.postValue(Resource.Loading())
            val response = cocktailRepository.getAllNoAlcoholicDrinks()
            cocktailNoAlcoholic.postValue(handleNoAlcoholicCocktailResponse(response))
        }catch (ex: Exception){

        }
    }

    private fun handleNoAlcoholicCocktailResponse(response: Response<CocktailList>): Resource<CocktailList> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}