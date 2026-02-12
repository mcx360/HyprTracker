package io.github.mcx360.hyprtracker.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import io.github.mcx360.hyprtracker.ui.models.BottomNavItem

//note to self: possibly refactor later as currenly label string is hard coded
object Constants {
    val bottomNavItems = listOf(
        BottomNavItem(
            label = "Logging",
            icon = Icons.Filled.Home,
            route = Destinations.LOGGINGSCREEN.name
        ),
        BottomNavItem(
            label = "Medicince",
            icon = Icons.Filled.Star,
            route = Destinations.MEDICINESCREEN.name
        ),
        BottomNavItem(
            label = "Graphs",
            icon = Icons.Filled.Info,
            route = Destinations.GRAPHVIEWSCREEN.name
        )
    )
}