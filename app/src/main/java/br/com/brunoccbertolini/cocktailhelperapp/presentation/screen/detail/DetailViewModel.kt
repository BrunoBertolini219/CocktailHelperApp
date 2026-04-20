package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetDrinkDetailUseCase
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
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDrinkDetail: GetDrinkDetailUseCase,
    private val saveFavoriteDrink: SaveFavoriteDrinkUseCase
) : ViewModel() {

    private val idDrink: String = checkNotNull(savedStateHandle["idDrink"]) { "idDrink must not be null" }
    private val strDrink: String = savedStateHandle.get<String>("strDrink") ?: ""

    private val _state = MutableStateFlow(DetailState(drinkName = strDrink))
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private val _events = Channel<DetailEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadDrink()
    }

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.Retry -> loadDrink()
            is DetailAction.SaveFavorite -> saveFavorite()
            is DetailAction.NavigateUp -> viewModelScope.launch {
                _events.send(DetailEvent.NavigateUp)
            }
        }
    }

    private fun loadDrink() = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        when (val result = getDrinkDetail(idDrink)) {
            is Resource.Success -> _state.update { it.copy(isLoading = false, drink = result.data) }
            is Resource.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
            is Resource.Loading -> Unit
        }
    }

    private fun saveFavorite() = viewModelScope.launch {
        val drink = _state.value.drink ?: return@launch
        saveFavoriteDrink(
            DrinkSummary(
                id = drink.id,
                name = drink.name,
                thumbnailUrl = drink.thumbnailUrl
            )
        )
        _events.send(DetailEvent.ShowSnackbar("Saved!"))
    }
}
