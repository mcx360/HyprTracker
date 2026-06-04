package io.github.mcx360.hyprtracker.ui.mainScreen.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.mcx360.hyprtracker.ui.graphScreen.GraphScreen
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.graphScreen.InsightsViewModel
import io.github.mcx360.hyprtracker.ui.historyScreen.HistoryTab
import io.github.mcx360.hyprtracker.ui.loggingScreen.LoggingScreenContainer
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineScreen
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel

@Composable
fun NavHostContainer(navController: NavHostController, hyprTrackerViewModel: HyprTrackerViewModel, snackBarHostState: SnackbarHostState, openAddMedicationScreen: MutableState<Boolean>, medicineViewModel: MedicineViewModel, insightsViewModel: InsightsViewModel) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Logging.name,
    ){
        composable(route = Destinations.Logging.name){
            LoggingScreenContainer(hyprTrackerViewModel = hyprTrackerViewModel, snackBarHostState = snackBarHostState)
        }
        composable(route = Destinations.History.name){
            HistoryTab(hyprTrackerViewModel = hyprTrackerViewModel, snackBarHostState = snackBarHostState)
        }
        composable(route = Destinations.Medicines.name){
            MedicineScreen(openAddMedicationScreen = openAddMedicationScreen, snackBarHostState = snackBarHostState, medicineViewModel = medicineViewModel)
        }
        composable(route = Destinations.Insight.name){
            GraphScreen(insightsViewModel = insightsViewModel)
        }
    }
}