package br.com.brunoccbertolini.cocktailhelperapp.ui.pages.alcoholic

import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetAlcoholicDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetNonAlcoholicDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist.CocktailListViewModel
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import org.junit.Before

class AlcoholicCocktailViewModelTest {

    private lateinit var viewModel: CocktailListViewModel
    private lateinit var repository: FakeCocktailRepository

    @Before
    fun setup() {
        repository = FakeCocktailRepository()
        viewModel = CocktailListViewModel(
            getAlcoholicDrinks = GetAlcoholicDrinksUseCase(repository),
            getNonAlcoholicDrinks = GetNonAlcoholicDrinksUseCase(repository)
        )
    }
}
