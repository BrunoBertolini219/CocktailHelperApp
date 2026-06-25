package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import br.com.brunoccbertolini.cocktailhelperapp.MainCoroutineRule
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.DrinkSummary
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.DeleteFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetDrinkDetailUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.GetFavoriteDrinksUseCase
import br.com.brunoccbertolini.cocktailhelperapp.domain.usecase.SaveFavoriteDrinkUseCase
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakeCocktailRepository
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakePreferencesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val repository = FakeCocktailRepository()
    private val preferencesRepository = FakePreferencesRepository()

    private fun buildViewModel() = DetailViewModel(
        savedStateHandle = SavedStateHandle(mapOf("idDrink" to "1", "strDrink" to "Mojito")),
        getDrinkDetail = GetDrinkDetailUseCase(repository),
        getFavorites = GetFavoriteDrinksUseCase(repository),
        saveFavoriteDrink = SaveFavoriteDrinkUseCase(repository),
        deleteFavoriteDrink = DeleteFavoriteDrinkUseCase(repository),
        preferencesRepository = preferencesRepository
    )

    @Test
    fun `loads drink detail into state`() = runTest {
        val viewModel = buildViewModel()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.drink).isNotNull()
        assertThat(state.error).isNull()
    }

    @Test
    fun `error loading sets error and no drink`() = runTest {
        repository.shouldReturnNetworkError = true

        val viewModel = buildViewModel()

        assertThat(viewModel.state.value.error).isNotNull()
        assertThat(viewModel.state.value.drink).isNull()
    }

    @Test
    fun `toggling favorite saves it and emits a snackbar`() = runTest {
        val viewModel = buildViewModel()

        viewModel.events.test {
            viewModel.onAction(DetailAction.ToggleFavorite)
            assertThat(awaitItem()).isInstanceOf(DetailEvent.ShowSnackbar::class.java)
            cancelAndIgnoreRemainingEvents()
        }
        assertThat(viewModel.state.value.isFavorite).isTrue()
    }

    @Test
    fun `favorite state reflects the favorites repository`() = runTest {
        repository.saveFavorite(DrinkSummary("1", "Mojito", null))

        val viewModel = buildViewModel()

        assertThat(viewModel.state.value.isFavorite).isTrue()
    }

    @Test
    fun `navigate up emits NavigateUp event`() = runTest {
        val viewModel = buildViewModel()

        viewModel.events.test {
            viewModel.onAction(DetailAction.NavigateUp)
            assertThat(awaitItem()).isInstanceOf(DetailEvent.NavigateUp::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
