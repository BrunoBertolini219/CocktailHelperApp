package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brunoccbertolini.cocktailhelperapp.getOrAwaitValueTest
import br.com.brunoccbertolini.cocktailhelperapp.model.Drink
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import com.google.common.truth.ExpectFailure.assertThat
import org.junit.Assert.assertEquals

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    private lateinit var viewModel: DetailViewModel


    @Before
    fun setup(){
        viewModel = DetailViewModel(FakeCocktailRepository())
    }

    @Test
    fun `insert a cocktail with empty field, returns error`(){
        viewModel.saveCocktail(DrinkPreview("10", "", "thumb"))

        val value = viewModel.drinkLiveData.getOrAwaitValueTest()

        assertEquals(value.getContentIfNotHandled(), Resource.Error<DetailViewModel>("ERROR"))

    }

    @Test
    fun `insert a cocktail with valid inputs, returns success`(){
        viewModel.saveCocktail(DrinkPreview("1", "pina", "ImageUrl"))

        val value = viewModel.drinkLiveData.getOrAwaitValueTest()

        assertEquals(value.getContentIfNotHandled()?.data, Resource.Success("Success"))

    }
}