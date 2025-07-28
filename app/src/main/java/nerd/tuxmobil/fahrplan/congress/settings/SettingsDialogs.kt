package nerd.tuxmobil.fahrplan.congress.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.designsystem.texts.Text
import nerd.tuxmobil.fahrplan.congress.designsystem.themes.EventFahrplanTheme

@Composable
internal fun ScheduleRefreshIntervalDialog(
    currentValue: Int,
    onOptionSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val entries = mapOf(
        -1 to stringResource(R.string.schedule_refresh_interval_title_unmodified),
        30000 to stringResource(R.string.schedule_refresh_interval_title_every_30_seconds),
        60000 to stringResource(R.string.schedule_refresh_interval_title_every_60_seconds),
        120000 to stringResource(R.string.schedule_refresh_interval_title_every_120_seconds),
    )

    PreferenceListDialog(
        title = stringResource(R.string.preference_dialog_title_schedule_refresh_interval),
        entries = entries,
        selectedOption = currentValue,
        onOptionSelected = onOptionSelected,
        onDismiss = onDismiss,
    )
}

@Composable
internal fun AlarmTimeDialog(
    currentValue: Int,
    onOptionSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val entries = mapOf(
        0 to stringResource(R.string.alarm_time_title_at_start_time),
        5 to stringResource(R.string.alarm_time_title_5_minutes_before),
        10 to stringResource(R.string.alarm_time_title_10_minutes_before),
        15 to stringResource(R.string.alarm_time_title_15_minutes_before),
        20 to stringResource(R.string.alarm_time_title_20_minutes_before),
        30 to stringResource(R.string.alarm_time_title_30_minutes_before),
        45 to stringResource(R.string.alarm_time_title_45_minutes_before),
        60 to stringResource(R.string.alarm_time_title_60_minutes_before),
    )

    PreferenceListDialog(
        title = stringResource(R.string.preference_dialog_title_alarm_time),
        entries = entries,
        selectedOption = currentValue,
        onOptionSelected = onOptionSelected,
        onDismiss = onDismiss,
    )
}

@Composable
internal fun <T> PreferenceListDialog(
    title: String,
    entries: Map<T, String>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        title = { Text(text = title) },
        text = {
            Column(modifier = Modifier.selectableGroup()) {
                entries.forEach { (value, description) ->
                    val isSelected = value == selectedOption
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = isSelected,
                                onClick = { onOptionSelected(value) },
                                role = Role.RadioButton
                            )
                            .minimumInteractiveComponentSize()
                            .padding(horizontal = 16.dp),
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = null
                        )
                        Text(
                            text = description,
                            style = EventFahrplanTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        onDismissRequest = onDismiss,
    )
}