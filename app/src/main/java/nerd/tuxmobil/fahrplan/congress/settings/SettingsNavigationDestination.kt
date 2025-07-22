package nerd.tuxmobil.fahrplan.congress.settings

internal sealed class SettingsNavigationDestination(val route: String)

internal data object ScheduleStatisticDestination : SettingsNavigationDestination("schedule_statistic")
