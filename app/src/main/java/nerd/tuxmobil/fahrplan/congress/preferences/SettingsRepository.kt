package nerd.tuxmobil.fahrplan.congress.preferences

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettingsStream(): Flow<Settings>

    suspend fun setUseDeviceTimeZone(enable: Boolean)
    suspend fun setAlternativeHighlighting(enable: Boolean)
    suspend fun setFastSwiping(enable: Boolean)
    suspend fun setAlarmTone(alarmToneUri: String?)
    suspend fun setInsistentAlarms(enable: Boolean)
    suspend fun setAlarmTime(alarmTime: Int)

    suspend fun setScheduleRefreshInterval(interval: Int)
    suspend fun setAutoUpdateEnabled(enable: Boolean)
    suspend fun setAlternativeScheduleUrl(url: String)
    suspend fun setEngelsystemShiftsUrl(url: String)
}