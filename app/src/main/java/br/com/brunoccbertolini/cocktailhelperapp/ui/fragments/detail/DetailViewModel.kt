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
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel() {
    private val _drinkLiveData = MutableLiveData<Event<Resource<DrinkList>>>()
    val drinkLiveData: LiveData<Event<Resource<DrinkList>>> get() = _drinkLiveData

    private var _drinkPreviewLiveData = MutableLiveData<Event<Resource<DrinkPreview>>>()
    val drinkPreviewLiveData: LiveData<Event<Resource<DrinkPreview>>> get() = _drinkPreviewLiveData

    fun getDrinkDetail(id: String) = viewModelScope.launch {
        _drinkLiveData.postValue(Event(Resource.Loading()))
        val response = repository.searchDrinkById(id)
        _drinkLiveData.postValue(Event(response))
    }

    fun saveCocktail(drink: DrinkPreview) = viewModelScope.launch {
        if (drink.strDrink.isNullOrEmpty() || drink.strDrinkThumb.isNullOrEmpty()) {
            _drinkPreviewLiveData.postValue(Event(Resource.Error("ERROR")))
        } else {
            repository.upsert(drink)
            _drinkPreviewLiveData.postValue(Event(Resource.Success(drink)))
        }
    }

    fun getRandomDrink() = viewModelScope.launch {
        _drinkLiveData.postValue(Event(Resource.Loading()))
        val response = repository.randomDrink()
        _drinkLiveData.postValue(Event(response))
    }

}
