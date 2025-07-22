package nerd.tuxmobil.fahrplan.congress.preferences

data class Settings(
    // Frontend preferences
    val isUseDeviceTimeZoneEnabled: Boolean = true,
    val isAlternativeHighlightingEnabled: Boolean = false,
    val isFastSwipingEnabled: Boolean = true,
    val alarmTone: String? = null,
    val isInsistentAlarmsEnabled: Boolean = false,
    val alarmTimeIndex: Int = 0,

    // Backend preferences
    val scheduleRefreshInterval: Int = 0,
    val isAutoUpdateEnabled: Boolean = true,
    val alternativeScheduleUrl: String? = null,
    val engelsystemShiftsUrl: String? = null,
)
