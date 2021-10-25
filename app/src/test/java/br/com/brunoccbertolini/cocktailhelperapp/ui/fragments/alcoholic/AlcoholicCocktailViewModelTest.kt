package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.alcoholic

import org.junit.Assert.*
import org.junit.Before
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import org.junit.Test

class AlcoholicCocktailViewModelTest {

    private lateinit var viewModel: AlcoholicCocktailViewModel

    @Before
    fun setup(){
        viewModel = AlcoholicCocktailViewModel(FakeCocktailRepository())
    }



}