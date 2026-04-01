package io.github.mcx360.hyprtracker.ui.MainScreen.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.mcx360.hyprtracker.ui.GraphScreen.GraphScreen
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.LoggingScreen.LoggingScreen
import io.github.mcx360.hyprtracker.ui.MedicineScreen.MedicineScreen

@Composable
fun NavHostContainer(navController: NavHostController, hyprTrackerViewModel: HyprTrackerViewModel, snackBarHostState: SnackbarHostState, openAddMedicationScreen: MutableState<Boolean>) {
    NavHost(
        navController = navController,
        startDestination = Destinations.Logging.name,
        modifier = Modifier
    ){
        composable(route = Destinations.Logging.name){
            LoggingScreen(hyprTrackerViewModel = hyprTrackerViewModel, snackBarHostState = snackBarHostState)
        }
        composable(route = Destinations.Medicine.name){
            MedicineScreen(openAddMedicationScreen = openAddMedicationScreen, snackBarHostState = snackBarHostState, hyprTrackerViewModel = hyprTrackerViewModel)
        }
        composable(route = Destinations.Insight.name){
            GraphScreen()
        }
    }
}