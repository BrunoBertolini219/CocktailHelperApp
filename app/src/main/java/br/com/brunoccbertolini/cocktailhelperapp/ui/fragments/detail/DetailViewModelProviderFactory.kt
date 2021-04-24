package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.brunoccbertolini.cocktailhelperapp.repository.CocktailRepository

class DetailViewModelProviderFactory(
    val repository: CocktailRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(repository) as T
    }
}