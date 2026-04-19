package br.com.brunoccbertolini.cocktailhelperapp.ui.pages.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.repositories.CocktailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedCocktailViewModel @Inject constructor(
    private val repository: CocktailRepository
) : ViewModel() {

    val savedCocktails: Flow<List<DrinkPreview>> = repository.getSavedCocktails()

    fun deleteSavedCocktail(drink: DrinkPreview) = viewModelScope.launch {
        repository.deleteCocktail(drink)
    }
}
