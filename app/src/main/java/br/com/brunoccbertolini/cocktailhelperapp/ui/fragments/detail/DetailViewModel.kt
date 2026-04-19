package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkList
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Event
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel() {
    private val _drinkState = MutableStateFlow<Resource<DrinkList>>(Resource.Loading())
    val drinkState: StateFlow<Resource<DrinkList>> = _drinkState.asStateFlow()

    private val _drinkPreviewState = MutableStateFlow<Event<Resource<DrinkPreview>>?>(null)
    val drinkPreviewState: StateFlow<Event<Resource<DrinkPreview>>?> = _drinkPreviewState.asStateFlow()

    fun getDrinkDetail(id: String) = viewModelScope.launch {
        _drinkState.value = Resource.Loading()
        _drinkState.value = repository.searchDrinkById(id)
    }

    fun saveCocktail(drink: DrinkPreview) = viewModelScope.launch {
        if (drink.strDrink.isNullOrEmpty() || drink.strDrinkThumb.isNullOrEmpty()) {
            _drinkPreviewState.value = Event(Resource.Error("ERROR"))
        } else {
            repository.upsert(drink)
            _drinkPreviewState.value = Event(Resource.Success(drink))
        }
    }

    fun getRandomDrink() = viewModelScope.launch {
        _drinkState.value = Resource.Loading()
        _drinkState.value = repository.randomDrink()
    }
}
