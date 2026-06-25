package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.search

import br.com.brunoccbertolini.cocktailhelperapp.MainCoroutineRule
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.DeleteFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetFavoriteDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SaveFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SearchDrinksByIngredientUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SearchDrinksByNameUseCase
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository = FakeCocktailRepository()

    private fun buildViewModel() = SearchViewModel(
        searchByName = SearchDrinksByNameUseCase(repository),
        searchByIngredient = SearchDrinksByIngredientUseCase(repository),
        getFavorites = GetFavoriteDrinksUseCase(repository),
        saveFavoriteDrink = SaveFavoriteDrinkUseCase(repository),
        deleteFavoriteDrink = DeleteFavoriteDrinkUseCase(repository)
    )

    @Test
    fun `blank query keeps results null`() = runTest {
        val viewModel = buildViewModel()
        assertThat(viewModel.state.value.drinks).isNull()
    }

    @Test
    fun `query change triggers debounced search`() = runTest {
        val viewModel = buildViewModel()

        viewModel.onAction(SearchAction.QueryChanged("mojito"))
        mainCoroutineRule.dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.drinks).isEqualTo(repository.searchResult)
    }

    @Test
    fun `search error surfaces error`() = runTest {
        repository.shouldReturnNetworkError = true
        val viewModel = buildViewModel()

        viewModel.onAction(SearchAction.QueryChanged("mojito"))
        mainCoroutineRule.dispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.error).isNotNull()
        assertThat(viewModel.state.value.drinks).isNull()
    }

    @Test
    fun `toggling favorite updates favoriteIds`() = runTest {
        val viewModel = buildViewModel()
        val drink = repository.searchResult.first()

        viewModel.onAction(SearchAction.ToggleFavorite(drink))

        assertThat(viewModel.state.value.favoriteIds).contains(drink.id)
    }
}
