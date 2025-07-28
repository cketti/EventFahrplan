package nerd.tuxmobil.fahrplan.congress.settings

internal sealed interface SettingsEffect {
    data class NavigateTo(val destination: SettingsNavigationDestination) : SettingsEffect
    data object NavigateBack : SettingsEffect
    data object LaunchNotificationSettingsScreen : SettingsEffect
    data object PickAlarmTone : SettingsEffect
}