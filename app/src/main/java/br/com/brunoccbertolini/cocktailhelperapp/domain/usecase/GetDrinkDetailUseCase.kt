package br.com.brunoccbertolini.cocktailhelperapp.domain.usecase

import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.CocktailRepository
import javax.inject.Inject

class GetDrinkDetailUseCase @Inject constructor(private val repository: CocktailRepository) {
    suspend operator fun invoke(id: String) = repository.getDrinkDetail(id)
}
