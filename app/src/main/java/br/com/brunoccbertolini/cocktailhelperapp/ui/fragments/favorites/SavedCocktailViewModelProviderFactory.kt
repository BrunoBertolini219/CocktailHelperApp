package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository

class SavedCocktailViewModelProviderFactory(val repository: CocktailRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SavedCocktailViewModel(repository) as T
    }
}