package nerd.tuxmobil.fahrplan.congress.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.compose.content
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import nerd.tuxmobil.fahrplan.congress.extensions.withExtras
import nerd.tuxmobil.fahrplan.congress.schedulestatistic.ScheduleStatisticActivity

class NewSettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = content {
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        val pickAlarmToneLauncher = rememberLauncherForActivityResult(PickAlarmToneContract()) { uri ->
            if (uri != null) {
                viewModel.event(SettingsEvent.AlarmTonePicked(uri))
            }
        }

        val navController = rememberNavController()

        // TODO: is this the correct approach?
        val lifecycleOwner = LocalLifecycleOwner.current
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
                        launchSystemNotificationScreen()
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
                SettingsScreen(state, sendEvent = viewModel::event)
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
                //FIXME
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

    private fun launchSystemNotificationScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            startActivity(intent)
        }
    }
}
