package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.random

import app.cash.turbine.test
import br.com.brunoccbertolini.cocktailhelperapp.MainCoroutineRule
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.DeleteFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetFavoriteDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetRandomDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SaveFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakePreferencesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RandomDrinkViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository = FakeCocktailRepository()
    private val preferencesRepository = FakePreferencesRepository()

    private fun buildViewModel() = RandomDrinkViewModel(
        getRandomDrink = GetRandomDrinkUseCase(repository),
        getFavorites = GetFavoriteDrinksUseCase(repository),
        saveFavoriteDrink = SaveFavoriteDrinkUseCase(repository),
        deleteFavoriteDrink = DeleteFavoriteDrinkUseCase(repository),
        preferencesRepository = preferencesRepository
    )

    @Test
    fun `loads a random drink on init`() = runTest {
        val viewModel = buildViewModel()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.drink).isEqualTo(repository.randomDrink)
    }

    @Test
    fun `error loading random sets error`() = runTest {
        repository.shouldReturnNetworkError = true

        val viewModel = buildViewModel()

        assertThat(viewModel.state.value.error).isNotNull()
        assertThat(viewModel.state.value.drink).isNull()
    }

    @Test
    fun `toggling favorite saves it and emits a snackbar`() = runTest {
        val viewModel = buildViewModel()

        viewModel.events.test {
            viewModel.onAction(RandomDrinkAction.ToggleFavorite)
            assertThat(awaitItem()).isInstanceOf(RandomDrinkEvent.ShowSnackbar::class.java)
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(viewModel.state.value.isFavorite).isTrue()
    }
}
