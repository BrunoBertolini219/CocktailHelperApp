package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository

class SearchProviderViewModelFactory(val repository: CocktailRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchCocktailViewModel(repository) as T
    }
}