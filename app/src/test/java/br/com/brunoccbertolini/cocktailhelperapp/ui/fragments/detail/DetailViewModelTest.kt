package br.com.brunoccbertolini.cocktailhelperapp.ui.fragments.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.brunoccbertolini.cocktailhelperapp.MainCoroutineRule
import br.com.brunoccbertolini.cocktailhelperapp.getOrAwaitValueTest
import br.com.brunoccbertolini.cocktailhelperapp.model.DrinkPreview
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: DetailViewModel


    @Before
    fun setup(){
        viewModel = DetailViewModel(FakeCocktailRepository())
    }

    @Test
    fun `insert a cocktail with empty field, returns error`(){
        viewModel.saveCocktail(DrinkPreview("10", "", "thumb"))

        val value = viewModel.drinkPreviewLiveData.getOrAwaitValueTest()

        assertEquals("ERROR", value.getContentIfNotHandled()?.message)
    }

    @Test
    fun `insert a cocktail with valid inputs, returns success`(){
        viewModel.saveCocktail(DrinkPreview("1", "pina", "ImageUrl"))
        val value = viewModel.drinkPreviewLiveData.getOrAwaitValueTest()


        assertEquals(DrinkPreview("1", "pina", "ImageUrl"), value.getContentIfNotHandled()?.data)

    }
}