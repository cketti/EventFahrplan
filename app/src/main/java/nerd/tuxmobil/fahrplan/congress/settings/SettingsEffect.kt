package nerd.tuxmobil.fahrplan.congress.settings

internal sealed interface SettingsEffect {
    data class NavigateTo(val destination: SettingsNavigationDestination) : SettingsEffect
}