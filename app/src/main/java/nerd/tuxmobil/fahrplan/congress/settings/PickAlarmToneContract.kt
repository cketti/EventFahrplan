package nerd.tuxmobil.fahrplan.congress.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import nerd.tuxmobil.fahrplan.congress.extensions.getParcelableExtraCompat
import nerd.tuxmobil.fahrplan.congress.extensions.withExtras

class PickAlarmToneContract : ActivityResultContract<Uri?, Uri?>() {
    override fun createIntent(context: Context, input: Uri?): Intent {
        return Intent(RingtoneManager.ACTION_RINGTONE_PICKER).withExtras(
            RingtoneManager.EXTRA_RINGTONE_TYPE to RingtoneManager.TYPE_ALARM,
            RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT to true,
            RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT to true,
            RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI to Settings.System.DEFAULT_ALARM_ALERT_URI,
            RingtoneManager.EXTRA_RINGTONE_EXISTING_URI to input
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            intent.getParcelableExtraCompat<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
        } else {
            null
        }
    }
}