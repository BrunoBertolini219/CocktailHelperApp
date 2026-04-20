package br.com.brunoccbertolini.cocktailhelperapp.domain.usecase

import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import javax.inject.Inject

class GetAlcoholicDrinksUseCase @Inject constructor(private val repository: CocktailRepository) {
    operator fun invoke() = repository.getAlcoholicDrinks()
}
