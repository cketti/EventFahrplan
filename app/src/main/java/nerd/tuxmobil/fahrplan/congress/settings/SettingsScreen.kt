package nerd.tuxmobil.fahrplan.congress.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.designsystem.templates.Scaffold
import nerd.tuxmobil.fahrplan.congress.designsystem.themes.EventFahrplanTheme

private const val HORIZONTAL_PADDING_DP = 16
private const val PREFERENCE_VERTICAL_PADDING_DP = 16

@Composable
internal fun SettingsScreen(
    state: SettingsUiState,
    sendEvent: (SettingsEvent) -> Unit,
) {
    EventFahrplanTheme {
        Scaffold { contentPadding ->
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(contentPadding)
            ) {
                Category(text = stringResource(R.string.development_settings)) {
                    ClickPreference(
                        title = stringResource(R.string.preference_title_schedule_refresh_interval),
                        subtitle = state.settings.scheduleRefreshIntervalToUiString(),
                        onClick = { sendEvent(SettingsEvent.ScheduleRefreshIntervalClicked) },
                    )

                    ClickPreference(
                        title = stringResource(R.string.preference_title_schedule_statistic),
                        subtitle = stringResource(R.string.preference_summary_schedule_statistic),
                        onClick = { sendEvent(SettingsEvent.ScheduleStatisticClicked) },
                    )
                }

                Category(text = stringResource(R.string.general_settings)) {
                    SwitchPreference(
                        title = stringResource(R.string.preference_title_auto_update_enabled),
                        subtitle = stringResource(R.string.preference_summary_auto_update_enabled),
                        checked = state.settings.isAutoUpdateEnabled,
                        onCheckedChange = { sendEvent(SettingsEvent.AutoUpdateClicked) },
                    )

                    SwitchPreference(
                        title = stringResource(R.string.preference_title_use_device_time_zone_enabled),
                        subtitle = stringResource(R.string.preference_summary_use_device_time_zone_enabled),
                        checked = state.settings.isUseDeviceTimeZoneEnabled,
                        onCheckedChange = { sendEvent(SettingsEvent.DeviceTimezoneClicked) },
                    )

                    // TODO: add icon for external action
                    ClickPreference(
                        title = stringResource(R.string.preference_title_app_notification_settings),
                        subtitle = stringResource(R.string.preference_summary_app_notification_settings),
                        onClick = { sendEvent(SettingsEvent.CustomizeNotificationsClicked) },
                    )

                    ClickPreference(
                        title = stringResource(R.string.preference_title_alternative_schedule_url),
                        subtitle = stringResource(R.string.preference_summary_alternative_schedule_url),
                        onClick = { sendEvent(SettingsEvent.AlternativeScheduleUrlClicked) },
                    )

                    SwitchPreference(
                        title = stringResource(R.string.preference_title_alternative_highlighting_enabled),
                        subtitle = stringResource(R.string.preference_summary_alternative_highlighting_enabled),
                        checked = state.settings.isAlternativeHighlightingEnabled,
                        onCheckedChange = { sendEvent(SettingsEvent.AlternativeHighlightingClicked) },
                    )

                    SwitchPreference(
                        title = stringResource(R.string.preference_title_fast_swiping_enabled),
                        subtitle = stringResource(R.string.preference_summary_fast_swiping_enabled),
                        checked = state.settings.isFastSwipingEnabled,
                        onCheckedChange = { sendEvent(SettingsEvent.FastSwipingClicked) },
                    )
                }

                Category(text = stringResource(R.string.reminders)) {
                    // TODO: external icon
                    ClickPreference(
                        title = stringResource(R.string.preference_title_alarm_tone),
                        subtitle = stringResource(R.string.preference_summary_alarm_tone),
                        onClick = { sendEvent(SettingsEvent.AlarmToneClicked) },
                    )

                    SwitchPreference(
                        title = stringResource(R.string.preference_title_insistent_alarms_enabled),
                        subtitle = stringResource(R.string.preference_summary_insistent_alarms_enabled),
                        checked = state.settings.isInsistentAlarmsEnabled,
                        onCheckedChange = { sendEvent(SettingsEvent.InsistentAlarmClicked) },
                    )

                    ClickPreference(
                        title = stringResource(R.string.preference_dialog_title_alarm_time),
                        subtitle = state.settings.alarmTimeToUiString(),
                        onClick = { sendEvent(SettingsEvent.AlarmTimeClicked) },
                    )

                }

                Category(text = stringResource(R.string.preference_engelsystem_category_title)) {
                    ClickPreference(
                        title = stringResource(R.string.preference_title_engelsystem_json_export_url),
                        onClick = { sendEvent(SettingsEvent.EngelsystemUrlClicked) },
                    )
                }
            }
        }
    }
}

@Composable
internal fun Category(
    text: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = text,
            style = EventFahrplanTheme.typography.titleSmall,
            color = EventFahrplanTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = HORIZONTAL_PADDING_DP.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        content()
    }
}

@Composable
private fun PreferenceText(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = EventFahrplanTheme.typography.titleLarge,
        )

        if (subtitle != null) {
            Text(
                text = subtitle,
                style = EventFahrplanTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
internal fun ClickPreference(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: () -> Unit,
) {
    PreferenceText(
        title = title,
        subtitle = subtitle,
        modifier = modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clickable(onClick = onClick)
            .padding(
                horizontal = HORIZONTAL_PADDING_DP.dp,
                vertical = PREFERENCE_VERTICAL_PADDING_DP.dp,
            )
    )
}

@Composable
internal fun SwitchPreference(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .minimumInteractiveComponentSize()
            .clickable(onClick = { onCheckedChange(checked.not()) })
            .padding(
                horizontal = HORIZONTAL_PADDING_DP.dp,
                vertical = PREFERENCE_VERTICAL_PADDING_DP.dp,
            )
    ) {
        PreferenceText(
            title = title,
            subtitle = subtitle,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@PreviewLightDark
@Composable
internal fun SettingsScreenPreview() {
    SettingsScreen(SettingsUiState(), sendEvent = {})
}