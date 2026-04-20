package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetAlcoholicDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetNonAlcoholicDrinksUseCase
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
class CocktailListViewModel @Inject constructor(
    private val getAlcoholicDrinks: GetAlcoholicDrinksUseCase,
    private val getNonAlcoholicDrinks: GetNonAlcoholicDrinksUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CocktailListState())
    val state: StateFlow<CocktailListState> = _state.asStateFlow()

    private val _events = Channel<CocktailListEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadAlcoholic()
        loadNonAlcoholic()
    }

    fun onAction(action: CocktailListAction) {
        when (action) {
            is CocktailListAction.Refresh -> refresh()
            is CocktailListAction.SelectTab -> _state.update { it.copy(selectedTab = action.index) }
            is CocktailListAction.DrinkClicked -> viewModelScope.launch {
                _events.send(CocktailListEvent.NavigateToDetail(action.drink))
            }
        }
    }

    private fun refresh() {
        if (_state.value.selectedTab == 0) loadAlcoholic(isRefreshing = true)
        else loadNonAlcoholic(isRefreshing = true)
    }

    private fun loadAlcoholic(isRefreshing: Boolean = false) = viewModelScope.launch {
        getAlcoholicDrinks().collect { result ->
            _state.update { state ->
                when (result) {
                    is Resource.Loading -> state.copy(
                        alcoholicLoading = !isRefreshing,
                        isRefreshing = isRefreshing && state.selectedTab == 0
                    )
                    is Resource.Success -> state.copy(
                        alcoholicDrinks = result.data ?: emptyList(),
                        alcoholicLoading = false,
                        isRefreshing = false,
                        alcoholicError = null
                    )
                    is Resource.Error -> state.copy(
                        alcoholicDrinks = result.data ?: state.alcoholicDrinks,
                        alcoholicLoading = false,
                        isRefreshing = false,
                        alcoholicError = result.message
                    )
                }
            }
        }
    }

    private fun loadNonAlcoholic(isRefreshing: Boolean = false) = viewModelScope.launch {
        getNonAlcoholicDrinks().collect { result ->
            _state.update { state ->
                when (result) {
                    is Resource.Loading -> state.copy(
                        nonAlcoholicLoading = !isRefreshing,
                        isRefreshing = isRefreshing && state.selectedTab == 1
                    )
                    is Resource.Success -> state.copy(
                        nonAlcoholicDrinks = result.data ?: emptyList(),
                        nonAlcoholicLoading = false,
                        isRefreshing = false,
                        nonAlcoholicError = null
                    )
                    is Resource.Error -> state.copy(
                        nonAlcoholicDrinks = result.data ?: state.nonAlcoholicDrinks,
                        nonAlcoholicLoading = false,
                        isRefreshing = false,
                        nonAlcoholicError = result.message
                    )
                }
            }
        }
    }
}
