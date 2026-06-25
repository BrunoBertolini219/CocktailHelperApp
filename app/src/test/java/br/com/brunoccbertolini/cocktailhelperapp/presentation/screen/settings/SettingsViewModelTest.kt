package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.settings

import app.cash.turbine.test
import br.com.brunoccbertolini.cocktailhelperapp.MainCoroutineRule
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.ThemeMode
import br.com.brunoccbertolini.cocktailhelperapp.repositories.FakePreferencesRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private val preferencesRepository = FakePreferencesRepository()

    private fun buildViewModel() = SettingsViewModel(preferencesRepository)

    @Test
    fun `setting theme mode updates state`() = runTest {
        val viewModel = buildViewModel()

        viewModel.state.test {
            assertThat(awaitItem().themeMode).isEqualTo(ThemeMode.SYSTEM)
            viewModel.onAction(SettingsAction.SetThemeMode(ThemeMode.DARK))
            assertThat(awaitItem().themeMode).isEqualTo(ThemeMode.DARK)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setting measure system updates state`() = runTest {
        val viewModel = buildViewModel()

        viewModel.state.test {
            assertThat(awaitItem().measureSystem).isEqualTo(MeasureSystem.Original)
            viewModel.onAction(SettingsAction.SetMeasureSystem(MeasureSystem.Metric))
            assertThat(awaitItem().measureSystem).isEqualTo(MeasureSystem.Metric)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
