package nerd.tuxmobil.fahrplan.congress.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nerd.tuxmobil.fahrplan.congress.preferences.Settings
import nerd.tuxmobil.fahrplan.congress.preferences.SettingsRepository
import nerd.tuxmobil.fahrplan.congress.repositories.AppRepository

class SettingsViewModel(
    private val settingsRepository: SettingsRepository = AppRepository.settingsRepository,
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = settingsRepository.getSettingsStream()
        .map { it.toSettingsUiModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    fun buttonClick() {
        viewModelScope.launch {
            settingsRepository.setAutoUpdateEnabled(uiState.value.settings.isAutoUpdateEnabled.not())
        }
    }
}

private fun Settings.toSettingsUiModel(): SettingsUiState {
    return SettingsUiState(
        settings = this,
    )
}