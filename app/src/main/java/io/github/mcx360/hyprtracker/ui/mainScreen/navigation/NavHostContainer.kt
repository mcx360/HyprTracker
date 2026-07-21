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
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineScreen
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel

@Composable
fun NavHostContainer(navController: NavHostController, hyprTrackerViewModel: HyprTrackerViewModel, snackBarHostState: SnackbarHostState, openAddMedicationScreen: MutableState<Boolean>, medicineViewModel: MedicineViewModel, insightsViewModel: InsightsViewModel, openAddBPlog: MutableState<Boolean>) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Logs.name,
    ){
        composable(route = Destinations.Logs.name){
            HistoryTab(hyprTrackerViewModel = hyprTrackerViewModel, snackBarHostState = snackBarHostState, openAddBPlog = openAddBPlog)
        }
        composable(route = Destinations.Medicine.name){
            MedicineScreen(openAddMedicationScreen = openAddMedicationScreen, snackBarHostState = snackBarHostState, medicineViewModel = medicineViewModel)
        }
        composable(route = Destinations.Insights.name){
            GraphScreen(insightsViewModel = insightsViewModel)
        }
    }
}