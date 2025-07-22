package nerd.tuxmobil.fahrplan.congress.settings

internal sealed interface SettingsEvent {
    data object AutoUpdateClicked : SettingsEvent
    data object ScheduleStatisticClicked : SettingsEvent
}