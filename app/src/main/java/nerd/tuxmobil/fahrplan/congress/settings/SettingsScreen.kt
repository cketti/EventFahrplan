package nerd.tuxmobil.fahrplan.congress.settings

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.core.net.toUri
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import nerd.tuxmobil.fahrplan.congress.schedulestatistic.ScheduleStatisticActivity

@Composable
internal fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val pickAlarmToneLauncher = rememberLauncherForActivityResult(PickAlarmToneContract()) { uri ->
        if (uri != null) {
            viewModel.event(SettingsEvent.AlarmTonePicked(uri))
        }
    }

    val navController = rememberNavController()

    // TODO: is this the correct approach?
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is SettingsEffect.NavigateTo -> {
                    navController.navigate(effect.destination.route)
                }
                SettingsEffect.NavigateBack -> {
                    navController.popBackStack()
                }
                SettingsEffect.LaunchNotificationSettingsScreen -> {
                    context.launchSystemNotificationScreen()
                }
                SettingsEffect.PickAlarmTone -> {
                    pickAlarmToneLauncher.launch(state.settings.alarmTone?.toUri())
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = SettingsNavigationDestination.SettingsList.route,
    ) {
        composable(route = SettingsNavigationDestination.SettingsList.route) {
            SettingsListScreen(state, sendEvent = viewModel::event)
        }
        dialog(route = SettingsNavigationDestination.ScheduleRefreshInterval.route) {
            ScheduleRefreshIntervalDialog(
                currentValue = state.settings.scheduleRefreshInterval,
                onOptionSelected = {
                    viewModel.event(SettingsEvent.SetScheduleRefreshInterval(it))
                },
                onDismiss = { navController.popBackStack() }
            )
        }
        activity(route = SettingsNavigationDestination.ScheduleStatistic.route) {
            activityClass = ScheduleStatisticActivity::class
        }
        dialog(route = SettingsNavigationDestination.AlternativeScheduleUrl.route) {
            ScheduleUrlDialog(
                currentValue = state.settings.alternativeScheduleUrl,
                onValueChanged = { /*FIXME*/ },
                onDismiss = { navController.popBackStack() }
            )
        }
        dialog(route = SettingsNavigationDestination.AlarmTime.route) {
            AlarmTimeDialog(
                currentValue = state.settings.alarmTime,
                onOptionSelected = { viewModel.event(SettingsEvent.SetAlarmTime(it)) },
                onDismiss = { navController.popBackStack() }
            )
        }
        dialog(route = SettingsNavigationDestination.EngelSystemUrl.route) {
            //FIXME
        }
    }
}

private fun Context.launchSystemNotificationScreen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        startActivity(intent)
    }
}
