package br.com.brunoccbertolini.cocktailhelperapp.domain.usecase

import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import javax.inject.Inject

class GetRandomDrinkUseCase @Inject constructor(private val repository: CocktailRepository) {
    suspend operator fun invoke() = repository.getRandomDrink()
}
