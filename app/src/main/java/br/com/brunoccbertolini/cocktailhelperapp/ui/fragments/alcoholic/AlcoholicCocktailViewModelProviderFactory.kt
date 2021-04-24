package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository

class AlcoholicCocktailViewModelProviderFactory(
    val cocktailRepository: CocktailRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlcoholicCocktailViewModel(cocktailRepository) as T
    }
}