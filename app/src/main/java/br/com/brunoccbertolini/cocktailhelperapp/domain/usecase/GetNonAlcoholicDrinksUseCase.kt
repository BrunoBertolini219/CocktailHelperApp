package br.com.brunoccbertolini.cocktailhelperapp.domain.usecase

import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import javax.inject.Inject

class GetNonAlcoholicDrinksUseCase @Inject constructor(private val repository: CocktailRepository) {
    operator fun invoke() = repository.getNonAlcoholicDrinks()
}
