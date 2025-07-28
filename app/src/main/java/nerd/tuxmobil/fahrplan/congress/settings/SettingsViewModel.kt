package nerd.tuxmobil.fahrplan.congress.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import nerd.tuxmobil.fahrplan.congress.preferences.Settings
import nerd.tuxmobil.fahrplan.congress.preferences.SettingsRepository
import nerd.tuxmobil.fahrplan.congress.repositories.AppRepository

internal class SettingsViewModel(
    private val settingsRepository: SettingsRepository = AppRepository.settingsRepository,
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = settingsRepository.getSettingsStream()
        .map { it.toSettingsUiModel() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    private val effectsChannel = Channel<SettingsEffect>()
    val effects = effectsChannel.receiveAsFlow()

    fun event(event: SettingsEvent) {
        viewModelScope.launch {
            handleEvent(event)
        }
    }

    private suspend fun handleEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.ScheduleRefreshIntervalClicked -> navigateToScheduleRefreshInterval()
            is SettingsEvent.SetScheduleRefreshInterval -> updateScheduleRefreshInterval(event.refreshInterval)
            SettingsEvent.ScheduleStatisticClicked -> navigateToScheduleStatistic()

            SettingsEvent.AutoUpdateClicked -> toggleAutoUpdateEnabled()
            SettingsEvent.DeviceTimezoneClicked -> toggleUseDeviceTimeZoneEnabled()
            SettingsEvent.CustomizeNotificationsClicked -> launchNotificationSettingsScreen()
            SettingsEvent.AlternativeScheduleUrlClicked -> navigateToAlternativeScheduleUrl()
            SettingsEvent.AlternativeHighlightingClicked -> toggleAlternativeHighlightingEnabled()
            SettingsEvent.FastSwipingClicked -> toggleFastSwipingEnabled()

            SettingsEvent.AlarmToneClicked -> pickAlarmTone()
            SettingsEvent.InsistentAlarmClicked -> toggleInsistentAlarmsEnabled()
            SettingsEvent.AlarmTimeClicked -> navigateToAlarmTime()
            is SettingsEvent.AlarmTonePicked -> updateAlarmTone(event.alarmTone)
            is SettingsEvent.SetAlarmTime -> updateAlarmTime(event.alarmTime)

            SettingsEvent.EngelsystemUrlClicked -> navigateToEngelsystemUrl()
        }
    }

    private suspend fun navigateToScheduleRefreshInterval() {
        effectsChannel.send(
            SettingsEffect.NavigateTo(SettingsNavigationDestination.ScheduleRefreshInterval)
        )
    }

    private suspend fun updateScheduleRefreshInterval(refreshInterval: Int) {
        settingsRepository.setScheduleRefreshInterval(refreshInterval)
        navigateBack()
    }

    private suspend fun navigateToScheduleStatistic() {
        effectsChannel.send(
            SettingsEffect.NavigateTo(SettingsNavigationDestination.ScheduleStatistic)
        )
    }

    private suspend fun toggleAutoUpdateEnabled() {
        val autoUpdateEnabled = uiState.value.settings.isAutoUpdateEnabled
        settingsRepository.setAutoUpdateEnabled(autoUpdateEnabled.not())
    }

    private suspend fun toggleUseDeviceTimeZoneEnabled() {
        val useDeviceTimeZoneEnabled = uiState.value.settings.isUseDeviceTimeZoneEnabled
        settingsRepository.setUseDeviceTimeZone(useDeviceTimeZoneEnabled.not())
    }

    private suspend fun launchNotificationSettingsScreen() {
        effectsChannel.send(SettingsEffect.LaunchNotificationSettingsScreen)
    }

    private suspend fun navigateToAlternativeScheduleUrl() {
        effectsChannel.send(
            SettingsEffect.NavigateTo(SettingsNavigationDestination.AlternativeScheduleUrl)
        )
    }

    private suspend fun toggleAlternativeHighlightingEnabled() {
        val alternativeHighlightingEnabled = uiState.value.settings.isAlternativeHighlightingEnabled
        settingsRepository.setAlternativeHighlighting(alternativeHighlightingEnabled.not())
    }

    private suspend fun toggleFastSwipingEnabled() {
        val fastSwipingEnabled = uiState.value.settings.isFastSwipingEnabled
        settingsRepository.setFastSwiping(fastSwipingEnabled.not())
    }

    private suspend fun pickAlarmTone() {
        effectsChannel.send(SettingsEffect.PickAlarmTone)
    }

    private suspend fun toggleInsistentAlarmsEnabled() {
        val insistentAlarmsEnabled = uiState.value.settings.isInsistentAlarmsEnabled
        settingsRepository.setInsistentAlarms(insistentAlarmsEnabled.not())
    }

    private suspend fun navigateToAlarmTime() {
        effectsChannel.send(SettingsEffect.NavigateTo(SettingsNavigationDestination.AlarmTime))
    }

    private suspend fun updateAlarmTone(alarmTone: Uri) {
        settingsRepository.setAlarmTone(alarmTone.toString())
    }

    private suspend fun updateAlarmTime(alarmTime: Int) {
        settingsRepository.setAlarmTime(alarmTime)
        navigateBack()
    }

    private suspend fun navigateToEngelsystemUrl() {
        effectsChannel.send(SettingsEffect.NavigateTo(SettingsNavigationDestination.EngelSystemUrl))
    }

    private suspend fun navigateBack() {
        effectsChannel.send(SettingsEffect.NavigateBack)
    }
}

private fun Settings.toSettingsUiModel(): SettingsUiState {
    return SettingsUiState(
        settings = this,
    )
}