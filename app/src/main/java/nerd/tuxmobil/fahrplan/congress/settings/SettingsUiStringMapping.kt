package nerd.tuxmobil.fahrplan.congress.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.preferences.Settings

@Composable
internal fun Settings.scheduleRefreshIntervalToUiString(): String {
    return when (scheduleRefreshInterval) {
        0 -> stringResource(R.string.schedule_refresh_interval_title_unmodified)
        1 -> stringResource(R.string.schedule_refresh_interval_title_every_30_seconds)
        2 -> stringResource(R.string.schedule_refresh_interval_title_every_60_seconds)
        3 -> stringResource(R.string.schedule_refresh_interval_title_every_120_seconds)
        else -> "Unsupported value: $scheduleRefreshInterval"
    }
}

@Composable
internal fun Settings.alarmTimeIndexToUiString(): String? {
    //FIXME
    return null
}