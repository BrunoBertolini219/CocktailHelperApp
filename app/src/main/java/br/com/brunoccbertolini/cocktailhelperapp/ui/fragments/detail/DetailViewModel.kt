package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Event
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel() {
    private val _drinkLiveData = MutableLiveData<Event<Resource<DrinkList>>>()
    val drinkLiveData: LiveData<Event<Resource<DrinkList>>> get() = _drinkLiveData

    fun getDrinkDetail(id: String) = viewModelScope.launch {
        _drinkLiveData.postValue(Event(Resource.Loading()))
        val response = repository.searchDrinkById(id)
        _drinkLiveData.postValue(Event(response))
    }

    fun saveCocktail(drink: DrinkPreview) = viewModelScope.launch {
        repository.upsert(drink)
    }

    fun getRandomDrink() = viewModelScope.launch {
        _drinkLiveData.postValue(Event(Resource.Loading()))
        val response = repository.randomDrink()
        _drinkLiveData.postValue(Event(response))
    }

}
