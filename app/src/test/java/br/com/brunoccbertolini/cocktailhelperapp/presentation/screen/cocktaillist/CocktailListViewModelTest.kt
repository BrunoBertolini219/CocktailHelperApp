package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.cocktaillist

import app.cash.turbine.test
import br.com.brunoccbertolini.cocktailhelperapp.MainCoroutineRule
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.DeleteFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetAlcoholicDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetFavoriteDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetNonAlcoholicDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SaveFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CocktailListViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository = FakeCocktailRepository()

    private fun buildViewModel() = CocktailListViewModel(
        getAlcoholicDrinks = GetAlcoholicDrinksUseCase(repository),
        getNonAlcoholicDrinks = GetNonAlcoholicDrinksUseCase(repository),
        getFavorites = GetFavoriteDrinksUseCase(repository),
        saveFavoriteDrink = SaveFavoriteDrinkUseCase(repository),
        deleteFavoriteDrink = DeleteFavoriteDrinkUseCase(repository)
    )

    @Test
    fun `loads both lists into state on init`() = runTest {
        val viewModel = buildViewModel()

        val state = viewModel.state.value
        assertThat(state.alcoholicDrinks).isEqualTo(repository.alcoholicDrinks)
        assertThat(state.nonAlcoholicDrinks).isEqualTo(repository.nonAlcoholicDrinks)
        assertThat(state.alcoholicLoading).isFalse()
        assertThat(state.alcoholicError).isNull()
    }

    @Test
    fun `network error surfaces a UiText error and no drinks`() = runTest {
        repository.shouldReturnNetworkError = true

        val viewModel = buildViewModel()

        val state = viewModel.state.value
        assertThat(state.alcoholicError).isNotNull()
        assertThat(state.alcoholicDrinks).isEmpty()
    }

    @Test
    fun `selecting a tab updates selectedTab`() = runTest {
        val viewModel = buildViewModel()

        viewModel.onAction(CocktailListAction.SelectTab(1))

        assertThat(viewModel.state.value.selectedTab).isEqualTo(1)
    }

    @Test
    fun `clicking a drink emits NavigateToDetail`() = runTest {
        val viewModel = buildViewModel()
        val drink = repository.alcoholicDrinks.first()

        viewModel.events.test {
            viewModel.onAction(CocktailListAction.DrinkClicked(drink))
            val event = awaitItem()
            assertThat(event).isInstanceOf(CocktailListEvent.NavigateToDetail::class.java)
            assertThat((event as CocktailListEvent.NavigateToDetail).drink).isEqualTo(drink)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggling favorite adds then removes the drink id`() = runTest {
        val viewModel = buildViewModel()
        val drink = repository.alcoholicDrinks.first()

        viewModel.onAction(CocktailListAction.ToggleFavorite(drink))
        assertThat(viewModel.state.value.favoriteIds).contains(drink.id)

        viewModel.onAction(CocktailListAction.ToggleFavorite(drink))
        assertThat(viewModel.state.value.favoriteIds).doesNotContain(drink.id)
    }
}
