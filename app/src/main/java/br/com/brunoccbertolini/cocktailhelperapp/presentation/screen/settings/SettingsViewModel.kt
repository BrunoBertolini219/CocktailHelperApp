package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.brunoccbertolini.cocktailhelperapp.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    val state: StateFlow<SettingsState> = preferencesRepository.preferences
        .map { prefs ->
            SettingsState(
                themeMode = prefs.themeMode,
                useDynamicColor = prefs.useDynamicColor,
                measureSystem = prefs.measureSystem
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsState()
        )

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetThemeMode -> viewModelScope.launch {
                preferencesRepository.setThemeMode(action.mode)
            }
            is SettingsAction.SetDynamicColor -> viewModelScope.launch {
                preferencesRepository.setDynamicColor(action.enabled)
            }
            is SettingsAction.SetMeasureSystem -> viewModelScope.launch {
                preferencesRepository.setMeasureSystem(action.system)
            }
        }
    }
}
