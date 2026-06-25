package br.com.brunoccbertolini.cocktailhelperapp.domain.model

/** User-configurable app preferences (persisted). */
data class AppPreferences(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColor: Boolean = true,
    val measureSystem: MeasureSystem = MeasureSystem.Original
)
