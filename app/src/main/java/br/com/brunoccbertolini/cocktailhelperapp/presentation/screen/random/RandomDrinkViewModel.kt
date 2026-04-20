package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetRandomDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SaveFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomDrinkViewModel @Inject constructor(
    private val getRandomDrink: GetRandomDrinkUseCase,
    private val saveFavoriteDrink: SaveFavoriteDrinkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RandomDrinkState())
    val state: StateFlow<RandomDrinkState> = _state.asStateFlow()

    private val _events = Channel<RandomDrinkEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadRandom()
    }

    fun onAction(action: RandomDrinkAction) {
        when (action) {
            is RandomDrinkAction.Refresh -> loadRandom()
            is RandomDrinkAction.SaveFavorite -> saveFavorite()
        }
    }

    private fun loadRandom() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        when (val result = getRandomDrink()) {
            is Resource.Success -> _state.update { it.copy(isLoading = false, drink = result.data) }
            is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun saveFavorite() = viewModelScope.launch {
        val drink = _state.value.drink ?: return@launch
        saveFavoriteDrink(
            DrinkSummary(id = drink.id, name = drink.name, thumbnailUrl = drink.thumbnailUrl)
        )
        _events.send(RandomDrinkEvent.ShowSnackbar("Saved!"))
    }
}
