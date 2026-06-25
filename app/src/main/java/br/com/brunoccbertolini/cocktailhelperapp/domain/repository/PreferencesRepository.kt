package br.com.brunoccbertolini.cocktailhelperapp.domain.repository

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.AppPreferences
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val preferences: Flow<AppPreferences>
    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setDynamicColor(enabled: Boolean)
    suspend fun setMeasureSystem(system: MeasureSystem)
}
