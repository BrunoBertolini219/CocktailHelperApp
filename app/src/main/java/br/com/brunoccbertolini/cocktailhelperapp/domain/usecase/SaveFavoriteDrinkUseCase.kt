package br.com.brunoccbertolini.cocktailhelperapp.domain.usecase

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import javax.inject.Inject

class SaveFavoriteDrinkUseCase @Inject constructor(private val repository: CocktailRepository) {
    suspend operator fun invoke(drink: DrinkSummary) = repository.saveFavorite(drink)
}
