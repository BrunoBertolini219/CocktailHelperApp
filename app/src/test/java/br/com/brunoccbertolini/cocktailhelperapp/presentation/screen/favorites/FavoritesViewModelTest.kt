package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.favorites

import app.cash.turbine.test
import br.com.brunoccbertolini.cocktailhelperapp.MainCoroutineRule
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.DeleteFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetFavoriteDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository = FakeCocktailRepository()

    private fun buildViewModel() = FavoritesViewModel(
        getFavorites = GetFavoriteDrinksUseCase(repository),
        deleteFavorite = DeleteFavoriteDrinkUseCase(repository)
    )

    @Test
    fun `saved favorites appear in state`() = runTest {
        val drink = DrinkSummary("1", "Mojito", null)
        repository.saveFavorite(drink)

        val viewModel = buildViewModel()

        viewModel.state.test {
            assertThat(expectMostRecentItem().drinks).contains(drink)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `delete action removes the favorite from state`() = runTest {
        val drink = DrinkSummary("1", "Mojito", null)
        repository.saveFavorite(drink)
        val viewModel = buildViewModel()

        viewModel.state.test {
            assertThat(expectMostRecentItem().drinks).contains(drink)
            viewModel.onAction(FavoritesAction.DeleteDrink(drink))
            assertThat(awaitItem().drinks).doesNotContain(drink)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clicking a drink emits NavigateToDetail`() = runTest {
        val drink = DrinkSummary("1", "Mojito", null)
        val viewModel = buildViewModel()

        viewModel.events.test {
            viewModel.onAction(FavoritesAction.DrinkClicked(drink))
            val event = awaitItem()
            assertThat(event).isInstanceOf(FavoritesEvent.NavigateToDetail::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
