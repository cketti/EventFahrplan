package nerd.tuxmobil.fahrplan.congress.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import nerd.tuxmobil.fahrplan.congress.designsystem.texts.Text
import nerd.tuxmobil.fahrplan.congress.schedulestatistic.ScheduleStatisticActivity

class NewSettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = content {
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        val navController = rememberNavController()

        // TODO: is this the correct approach?
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(key1 = lifecycleOwner.lifecycle) {
            viewModel.effects.collect { effect ->
                when (effect) {
                    is SettingsEffect.NavigateTo -> {
                        navController.navigate(effect.destination.route)
                    }
                }
            }
        }

        NavHost(
            navController = navController,
            startDestination = "settings",
        ) {
            composable(route = "settings") {
                SettingsScreen(state, buttonClick = { viewModel.buttonClick() })
            }
            dialog(route = "refresh_interval") {
                AlertDialog(
                    title = { Text("Change setting") },
                    text = { Text("Refresh interval destination") },
                    confirmButton = {
                        TextButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { navController.popBackStack() }
                        ) {
                            Text("Dismiss")
                        }
                    },
                    onDismissRequest = { navController.popBackStack() }
                )

//                EventFahrplanTheme {
//                    Scaffold { contentPadding ->
//                        Box(
//                            Modifier
//                                .padding(contentPadding)
//                                .padding(16.dp)
//                        ) {
//                            Column {
//                                Text("Refresh interval destination")
//                            }
//                        }
//                    }
//                }
            }
            activity(route = ScheduleStatisticDestination.route) {
                activityClass = ScheduleStatisticActivity::class
            }
        }
    }
}