package br.com.brunoccbertolini.cocktailhelperapp.ui.pages.detail

import androidx.lifecycle.SavedStateHandle
import br.com.brunoccbertolini.cocktailhelperapp.MainCoroutineRule
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetDrinkDetailUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SaveFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail.DetailAction
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail.DetailEvent
import br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail.DetailViewModel
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DetailViewModel
    private lateinit var repository: FakeCocktailRepository

    @Before
    fun setup() {
        repository = FakeCocktailRepository()
        val savedStateHandle = SavedStateHandle(
            mapOf("idDrink" to "1", "strDrink" to "Test Drink", "strDrinkThumb" to null)
        )
        viewModel = DetailViewModel(
            savedStateHandle = savedStateHandle,
            getDrinkDetail = GetDrinkDetailUseCase(repository),
            saveFavoriteDrink = SaveFavoriteDrinkUseCase(repository)
        )
    }

    @Test
    fun `load drink successfully updates state`() = runTest {
        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertTrue(state.drink != null)
    }

    @Test
    fun `save favorite emits ShowSnackbar event`() = runTest {
        // Wait for drink to load
        val state = viewModel.state.first { it.drink != null }
        viewModel.onAction(DetailAction.SaveFavorite)
        val event = viewModel.events.first()
        assertTrue(event is DetailEvent.ShowSnackbar)
    }
}
