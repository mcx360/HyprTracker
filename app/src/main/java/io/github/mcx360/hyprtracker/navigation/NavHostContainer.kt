package io.github.mcx360.hyprtracker.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.github.mcx360.hyprtracker.ui.GraphScreen
import io.github.mcx360.hyprtracker.ui.LoggingScreen
import io.github.mcx360.hyprtracker.ui.MedicineScreen

@Composable
fun NavHostContainer(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destinations.LOGGINGSCREEN.name,
        modifier = Modifier
    ){
        composable(route = Destinations.LOGGINGSCREEN.name){
            LoggingScreen()
        }
        composable(route = Destinations.MEDICINESCREEN.name){
            MedicineScreen()
        }
        composable(route = Destinations.GRAPHVIEWSCREEN.name){
            GraphScreen()
        }
    }
}