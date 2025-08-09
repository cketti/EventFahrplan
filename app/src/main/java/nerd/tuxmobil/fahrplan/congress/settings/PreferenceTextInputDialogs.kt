package nerd.tuxmobil.fahrplan.congress.settings

import android.annotation.SuppressLint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import nerd.tuxmobil.fahrplan.congress.R
import nerd.tuxmobil.fahrplan.congress.designsystem.texts.Text

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun ScheduleUrlDialog(
    currentValue: String?,
    onValueChanged: (String) -> Unit,
    onDismiss: () -> Unit
) {
    PreferenceTextInputDialog(
        title = stringResource(R.string.preference_title_alternative_schedule_url),
        value = currentValue.orEmpty(),
        onValueChanged = onValueChanged,
        onDismiss = onDismiss,
    )
}

@Composable
internal fun PreferenceTextInputDialog(
    title: String,
    value: String,
    onValueChanged: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var currentValue by remember { mutableStateOf(value) }

    AlertDialog(
        title = { Text(text = title) },
        text = {
            OutlinedTextField(
                value = currentValue,
                onValueChange = { currentValue = it },
            )
        },
        confirmButton = {
            TextButton(onClick = { onValueChanged(currentValue) }) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        onDismissRequest = onDismiss,
    )
}

@PreviewLightDark
@Composable
internal fun ScheduleUrlDialogPreview() {
    ScheduleUrlDialog(
        currentValue = "",
        onValueChanged = {},
        onDismiss = {}
    )
}