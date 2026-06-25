package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.settings

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.ThemeMode

sealed interface SettingsAction {
    data class SetThemeMode(val mode: ThemeMode) : SettingsAction
    data class SetDynamicColor(val enabled: Boolean) : SettingsAction
    data class SetMeasureSystem(val system: MeasureSystem) : SettingsAction
}
