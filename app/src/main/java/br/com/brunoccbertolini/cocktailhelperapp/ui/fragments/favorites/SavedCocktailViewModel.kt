package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository
import kotlinx.coroutines.launch

class SavedCocktailViewModel(val repository: CocktailRepository) : ViewModel() {

    fun getSavedCocktails() = repository.getSavedCocktails()

    fun deleteSavedCocktail(drink: DrinkPreview) = viewModelScope.launch {
        repository.deleteCocktail(drink)
    }

}