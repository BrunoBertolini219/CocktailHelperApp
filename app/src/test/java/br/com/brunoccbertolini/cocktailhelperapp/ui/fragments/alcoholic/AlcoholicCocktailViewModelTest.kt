package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import org.junit.Before

class AlcoholicCocktailViewModelTest {

    private lateinit var viewModel: AlcoholicCocktailViewModel

    @Before
    fun setup(){
        viewModel = AlcoholicCocktailViewModel(FakeCocktailRepository())
    }



}