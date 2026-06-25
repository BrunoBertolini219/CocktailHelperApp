package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.DeleteFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetFavoriteDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SaveFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SearchDrinksByIngredientUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SearchDrinksByNameUseCase
import br.com.brunoccbertolini.cocktailhelperapp.presentation.util.toUiText
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchByName: SearchDrinksByNameUseCase,
    private val searchByIngredient: SearchDrinksByIngredientUseCase,
    getFavorites: GetFavoriteDrinksUseCase,
    private val saveFavoriteDrink: SaveFavoriteDrinkUseCase,
    private val deleteFavoriteDrink: DeleteFavoriteDrinkUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _events = Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    private data class SearchInput(val query: String, val type: SearchType)

    private val _searchInput = MutableStateFlow(SearchInput("", SearchType.Name))

    init {
        viewModelScope.launch {
            _searchInput
                .debounce(500)
                .filter { it.query.isNotBlank() }
                .collect { input -> performSearch(input.query, input.type) }
        }
        getFavorites()
            .onEach { favorites ->
                _state.update { it.copy(favoriteIds = favorites.mapTo(HashSet()) { drink -> drink.id }) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.QueryChanged -> {
                _state.update { it.copy(query = action.query, drinks = if (action.query.isBlank()) null else it.drinks) }
                _searchInput.value = SearchInput(action.query, _state.value.searchType)
            }
            is SearchAction.SearchTypeChanged -> {
                _state.update { it.copy(searchType = action.type) }
                _searchInput.value = SearchInput(_state.value.query, action.type)
            }
            is SearchAction.DrinkClicked -> viewModelScope.launch {
                _events.send(SearchEvent.NavigateToDetail(action.drink))
            }
            is SearchAction.ToggleFavorite -> toggleFavorite(action.drink)
        }
    }

    private fun toggleFavorite(drink: DrinkSummary) = viewModelScope.launch {
        if (_state.value.favoriteIds.contains(drink.id)) {
            deleteFavoriteDrink(drink)
        } else {
            saveFavoriteDrink(drink)
        }
    }

    private fun performSearch(query: String, type: SearchType) = viewModelScope.launch {
        _state.update { it.copy(isLoading = true, error = null) }
        val result = when (type) {
            SearchType.Name -> searchByName(query)
            SearchType.Ingredient -> searchByIngredient(query)
        }
        _state.update { state ->
            when (result) {
                is Resource.Success -> state.copy(isLoading = false, drinks = result.data)
                is Resource.Error -> state.copy(isLoading = false, error = result.error.toUiText(), drinks = null)
                is Resource.Loading -> state
            }
        }
    }
}
