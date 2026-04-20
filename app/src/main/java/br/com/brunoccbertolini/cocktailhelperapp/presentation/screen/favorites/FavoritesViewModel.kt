package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.DeleteFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetFavoriteDrinksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavorites: GetFavoriteDrinksUseCase,
    private val deleteFavorite: DeleteFavoriteDrinkUseCase
) : ViewModel() {

    val state: StateFlow<FavoritesState> = getFavorites()
        .map { drinks -> FavoritesState(drinks = drinks) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavoritesState()
        )

    private val _events = Channel<FavoritesEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: FavoritesAction) {
        when (action) {
            is FavoritesAction.DrinkClicked -> viewModelScope.launch {
                _events.send(FavoritesEvent.NavigateToDetail(action.drink))
            }
            is FavoritesAction.DeleteDrink -> viewModelScope.launch {
                deleteFavorite(action.drink)
            }
        }
    }
}
