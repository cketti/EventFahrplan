package nerd.tuxmobil.fahrplan.congress.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nerd.tuxmobil.fahrplan.congress.designsystem.templates.Scaffold
import nerd.tuxmobil.fahrplan.congress.designsystem.texts.Text
import nerd.tuxmobil.fahrplan.congress.designsystem.themes.EventFahrplanTheme

@Composable
internal fun SettingsScreen(
    state: SettingsUiState,
    sendEvent: (SettingsEvent) -> Unit,
) {
    EventFahrplanTheme {
        Scaffold { contentPadding ->
            Box(
                Modifier
                    .padding(contentPadding)
                    .padding(16.dp)
            ) {
                Column {
                    Text("Development")
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Choose schedule refresh interval")
                    Text(
                        text = "Schedule statistic",
                        modifier = Modifier.clickable { sendEvent(SettingsEvent.ScheduleStatisticClicked) },
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text("General")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Text("Enable automatic updates")
                        Switch(
                            checked = state.settings.isAutoUpdateEnabled,
                            onCheckedChange = {
                                sendEvent(SettingsEvent.AutoUpdateClicked)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text("isUseDeviceTimeZoneEnabled: ${state.settings.isUseDeviceTimeZoneEnabled}")
                    Text("isAutoUpdateEnabled: ${state.settings.isAutoUpdateEnabled}")
                }
            }
        }
    }
}

@Preview
@Composable
internal fun SettingsScreenPreview() {
    SettingsScreen(SettingsUiState(), sendEvent = {})
}