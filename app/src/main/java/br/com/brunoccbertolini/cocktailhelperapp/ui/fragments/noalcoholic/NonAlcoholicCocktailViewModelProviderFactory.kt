package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.noalcoholic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository

class NonAlcoholicCocktailViewModelProviderFactory(
    val cocktailRepository: CocktailRepository
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NonAlcoholicCocktailViewModel(cocktailRepository) as T
    }
}