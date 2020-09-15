package nerd.tuxmobil.fahrplan.congress.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import nerd.tuxmobil.fahrplan.congress.R

class SharedPreferencesRepository(val context: Context) {

    private companion object {

        const val CHANGES_SEEN_KEY = "nerd.tuxmobil.fahrplan.congress.Prefs.CHANGES_SEEN"
        const val DISPLAY_DAY_INDEX_KEY = "nerd.tuxmobil.fahrplan.congress.Prefs.DISPLAY_DAY_INDEX"
        const val ENGELSYSTEM_SHIFTS_HASH_KEY = "nerd.tuxmobil.fahrplan.congress.Prefs.ENGELSYSTEM_SHIFTS_HASH"
        const val SCHEDULE_LAST_FETCHED_AT_KEY = "nerd.tuxmobil.fahrplan.congress.Prefs.SCHEDULE_LAST_FETCHED_AT"

    }

    private val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    init {
        PreferenceManager.setDefaultValues(context, R.xml.prefs, false)
    }

    fun getAlarmTimeIndex(): Int {
        val key = context.getString(R.string.preference_key_alarm_time_index)
        val defaultValue = context.getString(R.string.preference_default_value_alarm_time_value)
        val value = preferences.getString(key, defaultValue)!!
        val entryValues = context.resources.getStringArray(R.array.preference_entry_values_alarm_time)
        val defaultIndex = context.resources.getInteger(R.integer.preference_default_value_alarm_time_index)
        val index = entryValues.indexOf(value)
        return if (index == -1) defaultIndex else index
    }

    fun getAlarmTone(): String? {
        val key = context.getString(R.string.preference_key_alarm_tone)
        val defaultValue = AlarmTonePreference.DEFAULT_VALUE_STRING
        return preferences.getString(key, defaultValue)
    }

    fun isAlternativeHighlightingEnabled(): Boolean {
        val key = context.getString(R.string.preference_key_alternative_highlighting_enabled)
        val defaultValue = context.resources.getBoolean(R.bool.preference_default_value_alternative_highlighting_enabled)
        return preferences.getBoolean(key, defaultValue)
    }

    fun isAutoUpdateEnabled(): Boolean {
        val key = context.getString(R.string.preference_key_auto_update_enabled)
        val defaultValue = context.resources.getBoolean(R.bool.preference_default_value_auto_update_enabled)
        return preferences.getBoolean(key, defaultValue)
    }

    fun getDisplayDayIndex() = preferences.getInt(DISPLAY_DAY_INDEX_KEY, 1)

    fun setDisplayDayIndex(displayDayIndex: Int) = preferences.edit {
        putInt(DISPLAY_DAY_INDEX_KEY, displayDayIndex)
    }

    fun isInsistentAlarmsEnabled(): Boolean {
        val key = context.getString(R.string.preference_key_insistent_alarms_enabled)
        val defaultValue = context.resources.getBoolean(R.bool.preference_default_value_insistent_alarms_enabled)
        return preferences.getBoolean(key, defaultValue)
    }

    fun getScheduleLastFetchedAt() =
            preferences.getLong(SCHEDULE_LAST_FETCHED_AT_KEY, 0)

    fun setScheduleLastFetchedAt(fetchedAt: Long) = preferences.edit {
        putLong(SCHEDULE_LAST_FETCHED_AT_KEY, fetchedAt)
    }

    fun getChangesSeen() =
            preferences.getBoolean(CHANGES_SEEN_KEY, true)

    fun setChangesSeen(changesSeen: Boolean) = preferences.edit {
        putBoolean(CHANGES_SEEN_KEY, changesSeen)
    }

    fun getAlternativeScheduleUrl(): String {
        val key = context.getString(R.string.preference_key_alternative_schedule_url)
        val defaultValue = context.getString(R.string.preference_default_value_alternative_schedule_url)
        return preferences.getString(key, defaultValue)!!
    }

    fun getEngelsystemShiftsUrl(): String {
        val key = context.getString(R.string.preference_key_engelsystem_json_export_url)
        val defaultValue = context.getString(R.string.preference_default_value_engelsystem_json_export_url)
        return preferences.getString(key, defaultValue)!!
    }

    fun getLastEngelsystemShiftsHash() =
            preferences.getInt(ENGELSYSTEM_SHIFTS_HASH_KEY, 0)

    fun setLastEngelsystemShiftsHash(hash: Int) = preferences.edit {
        putInt(ENGELSYSTEM_SHIFTS_HASH_KEY, hash)
    }

}
