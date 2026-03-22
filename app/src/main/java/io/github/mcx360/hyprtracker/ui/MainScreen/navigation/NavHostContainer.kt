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
        startDestination = Destinations.LOGGINGSCREEN.name,
        modifier = Modifier
    ){
        composable(route = Destinations.LOGGINGSCREEN.name){
            LoggingScreen(hyprTrackerViewModel = hyprTrackerViewModel, snackBarHostState = snackBarHostState)
        }
        composable(route = Destinations.MEDICINESCREEN.name){
            MedicineScreen(openAddMedicationScreen = openAddMedicationScreen)
        }
        composable(route = Destinations.GRAPHVIEWSCREEN.name){
            GraphScreen()
        }
    }
}