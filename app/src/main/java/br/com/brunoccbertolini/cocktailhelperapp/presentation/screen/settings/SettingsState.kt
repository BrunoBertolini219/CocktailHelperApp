package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.settings

import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.ThemeMode

data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColor: Boolean = true,
    val measureSystem: MeasureSystem = MeasureSystem.Original
)
