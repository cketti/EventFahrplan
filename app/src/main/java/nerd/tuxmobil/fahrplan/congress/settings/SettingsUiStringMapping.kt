package nerd.tuxmobil.fahrplan.congress.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.preferences.Settings

//TODO: Only use this mapping in one place (duplicated in SettingsDialogs)
@Composable
internal fun Settings.scheduleRefreshIntervalToUiString(): String {
    return when (scheduleRefreshInterval) {
        -1 -> stringResource(R.string.schedule_refresh_interval_title_unmodified)
        30000 -> stringResource(R.string.schedule_refresh_interval_title_every_30_seconds)
        60000 -> stringResource(R.string.schedule_refresh_interval_title_every_60_seconds)
        120000 -> stringResource(R.string.schedule_refresh_interval_title_every_120_seconds)
        else -> "Custom value: every ${scheduleRefreshInterval / 1000} seconds"
    }
}

@Composable
internal fun Settings.alarmTimeToUiString(): String? {
    return when (alarmTime) {
        0 -> stringResource(R.string.alarm_time_title_at_start_time)
        5 -> stringResource(R.string.alarm_time_title_5_minutes_before)
        10 -> stringResource(R.string.alarm_time_title_10_minutes_before)
        15 -> stringResource(R.string.alarm_time_title_15_minutes_before)
        20 -> stringResource(R.string.alarm_time_title_20_minutes_before)
        30 -> stringResource(R.string.alarm_time_title_30_minutes_before)
        45 -> stringResource(R.string.alarm_time_title_45_minutes_before)
        60 -> stringResource(R.string.alarm_time_title_60_minutes_before)
        else -> "Custom value: $alarmTime minutes before"
    }
}