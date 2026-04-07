package io.github.mcx360.hyprtracker.ui.mainScreen.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.mcx360.hyprtracker.ui.graphScreen.GraphScreen
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.loggingScreen.LoggingScreen
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineScreen

@Composable
fun NavHostContainer(navController: NavHostController, hyprTrackerViewModel: HyprTrackerViewModel, snackBarHostState: SnackbarHostState, openAddMedicationScreen: MutableState<Boolean>) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Logging.name,
    ){
        composable(route = Destinations.Logging.name){
            LoggingScreen(hyprTrackerViewModel = hyprTrackerViewModel, snackBarHostState = snackBarHostState)
        }
        composable(route = Destinations.Medicines.name){
            MedicineScreen(openAddMedicationScreen = openAddMedicationScreen, snackBarHostState = snackBarHostState)
        }
        composable(route = Destinations.Insight.name){
            GraphScreen()
        }
    }
}