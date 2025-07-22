package nerd.tuxmobil.fahrplan.congress.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
    buttonClick: () -> Unit,    //FIXME: get rid of this - replace with sendUiEvent
) {
    EventFahrplanTheme {
        Scaffold { contentPadding ->
            Box(
                Modifier
                    .padding(contentPadding)
                    .padding(16.dp)
            ) {
                Column {
                    Text("Hello world")
                    Text("isAutoUpdateEnabled: ${state.settings.isAutoUpdateEnabled}")
                    Text("isUseDeviceTimeZoneEnabled: ${state.settings.isUseDeviceTimeZoneEnabled}")

                    Spacer(modifier = Modifier.height(20.dp))
                    Button(onClick = buttonClick) {
                        Text("Button")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
internal fun SettingsScreenPreview() {
    SettingsScreen(SettingsUiState(), buttonClick = {})
}