package br.com.brunoccbertolini.cocktailhelperapp.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.AppPreferences
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.ThemeMode
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    private object Keys {
        val THEME = stringPreferencesKey("theme_mode")
        val DYNAMIC = booleanPreferencesKey("dynamic_color")
        val MEASURE = stringPreferencesKey("measure_system")
    }

    override val preferences: Flow<AppPreferences> = dataStore.data.map { prefs ->
        AppPreferences(
            themeMode = prefs[Keys.THEME]?.toEnumOrNull<ThemeMode>() ?: ThemeMode.SYSTEM,
            useDynamicColor = prefs[Keys.DYNAMIC] ?: true,
            measureSystem = prefs[Keys.MEASURE]?.toEnumOrNull<MeasureSystem>() ?: MeasureSystem.Original
        )
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.edit { it[Keys.THEME] = mode.name }
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        dataStore.edit { it[Keys.DYNAMIC] = enabled }
    }

    override suspend fun setMeasureSystem(system: MeasureSystem) {
        dataStore.edit { it[Keys.MEASURE] = system.name }
    }
}

private inline fun <reified T : Enum<T>> String.toEnumOrNull(): T? =
    runCatching { enumValueOf<T>(this) }.getOrNull()
