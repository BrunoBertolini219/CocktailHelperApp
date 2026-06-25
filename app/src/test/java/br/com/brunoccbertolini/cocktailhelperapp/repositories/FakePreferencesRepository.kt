package br.com.brunoccbertolini.cocktailhelperapp.repositories

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.AppPreferences
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.ThemeMode
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakePreferencesRepository : PreferencesRepository {

    private val _preferences = MutableStateFlow(AppPreferences())
    override val preferences: Flow<AppPreferences> = _preferences.asStateFlow()

    override suspend fun setThemeMode(mode: ThemeMode) {
        _preferences.value = _preferences.value.copy(themeMode = mode)
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        _preferences.value = _preferences.value.copy(useDynamicColor = enabled)
    }

    override suspend fun setMeasureSystem(system: MeasureSystem) {
        _preferences.value = _preferences.value.copy(measureSystem = system)
    }
}
