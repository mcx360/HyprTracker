package io.github.mcx360.hyprtracker.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.Destinations
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.NavHostContainer
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.insightsScreen.InsightsViewModel
import io.github.mcx360.hyprtracker.ui.mainScreen.components.BottomNavBar
import io.github.mcx360.hyprtracker.ui.mainScreen.components.TopAppBar
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.Settings
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.ThemeViewModel
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HyprTrackerScreen(
    hyprTrackerViewModel: HyprTrackerViewModel = viewModel(factory = HyprTrackerViewModel.Factory),
    medicineViewModel: MedicineViewModel = viewModel(factory = MedicineViewModel.Factory),
    themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModel.Factory),
    insightsViewModel: InsightsViewModel = viewModel(factory = InsightsViewModel.Factory)
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackBarHostState = remember { SnackbarHostState() }
    val openAddMedicationScreen = remember { mutableStateOf(false) }
    val openSettingsDialog = remember { mutableStateOf(false) }
    val openAddBPLog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    title = currentRoute,
                    updateOpenSettings = {openSettingsDialog.value = true},
                    insightsViewModel = insightsViewModel
                )
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = currentRoute,
                    navController = navController,
                )
            },
            floatingActionButton = {
                if (currentRoute == Destinations.Medicine.name) {
                    FloatingActionButton(onClick = { openAddMedicationScreen.value = true}) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }else if (currentRoute == Destinations.Logs.name){
                    FloatingActionButton(onClick = { openAddBPLog.value = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = null)
                    }
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { innerpadding ->
            Box(modifier = Modifier.padding(innerpadding)) {
                key(currentRoute) {
                    NavHostContainer(
                        navController = navController,
                        hyprTrackerViewModel,
                        snackBarHostState,
                        openAddMedicationScreen,
                        medicineViewModel,
                        insightsViewModel,
                        openAddBPLog
                    )
                }

                when {
                    openSettingsDialog.value -> {
                        Settings(
                            onDismissRequest = { openSettingsDialog.value = false },
                            hyprTrackerViewModel = hyprTrackerViewModel,
                            medicineViewModel = medicineViewModel,
                            themeViewModel = themeViewModel
                        )
                    }
                }
            }
        }
    }
}