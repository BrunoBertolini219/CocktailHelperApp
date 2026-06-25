package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.PreferencesRepository
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.DeleteFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetFavoriteDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetRandomDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SaveFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.UiText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.toUiText
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RandomDrinkViewModel @Inject constructor(
    private val getRandomDrink: GetRandomDrinkUseCase,
    getFavorites: GetFavoriteDrinksUseCase,
    private val saveFavoriteDrink: SaveFavoriteDrinkUseCase,
    private val deleteFavoriteDrink: DeleteFavoriteDrinkUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RandomDrinkState())
    val state: StateFlow<RandomDrinkState> = _state.asStateFlow()

    private val _events = Channel<RandomDrinkEvent>()
    val events = _events.receiveAsFlow()

    private var favoriteIds: Set<String> = emptySet()

    init {
        loadRandom()
        getFavorites()
            .onEach { favorites ->
                favoriteIds = favorites.mapTo(HashSet()) { it.id }
                recomputeFavorite()
            }
            .launchIn(viewModelScope)
        preferencesRepository.preferences
            .onEach { prefs -> _state.update { it.copy(measureSystem = prefs.measureSystem) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: RandomDrinkAction) {
        when (action) {
            is RandomDrinkAction.Refresh -> loadRandom()
            is RandomDrinkAction.ToggleFavorite -> toggleFavorite()
            is RandomDrinkAction.SetMeasureSystem -> viewModelScope.launch {
                preferencesRepository.setMeasureSystem(action.system)
            }
        }
    }

    private fun loadRandom() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        when (val result = getRandomDrink()) {
            is Resource.Success -> {
                _state.update { it.copy(isLoading = false, drink = result.data) }
                recomputeFavorite()
            }
            is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.error.toUiText()) }
            is Resource.Loading -> Unit
        }
    }

    private fun recomputeFavorite() {
        _state.update { state ->
            val id = state.drink?.id
            state.copy(isFavorite = id != null && favoriteIds.contains(id))
        }
    }

    private fun toggleFavorite() = viewModelScope.launch {
        val drink = _state.value.drink ?: return@launch
        val summary = DrinkSummary(id = drink.id, name = drink.name, thumbnailUrl = drink.thumbnailUrl)
        if (_state.value.isFavorite) {
            deleteFavoriteDrink(summary)
            _events.send(RandomDrinkEvent.ShowSnackbar(UiText.StringResource(R.string.removed_message)))
        } else {
            saveFavoriteDrink(summary)
            _events.send(RandomDrinkEvent.ShowSnackbar(UiText.StringResource(R.string.saved_message)))
        }
    }
}
