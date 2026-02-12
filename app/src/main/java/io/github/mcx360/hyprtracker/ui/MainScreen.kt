package io.github.mcx360.hyprtracker.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.utils.Constants
import io.github.mcx360.hyprtracker.utils.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HyprTrackerTopAppBar(){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {/*to be implemented*/}) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun HyprTrackerBottomNavigationBar(navController: NavHostController){
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Constants.bottomNavItems.forEach{ navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route)
                },
                icon = {
                    Icon(imageVector = navItem.icon,
                        contentDescription = navItem.label)
                },
                label = {
                    Text(text = navItem.label,)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
) {
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

@Composable
fun HyprTrackerScreen(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()){
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        topBar = {
            HyprTrackerTopAppBar()
        },
        bottomBar = {
            HyprTrackerBottomNavigationBar(navController = navController)
        }
    ) { innerpadding ->
        NavHostContainer(navController = navController)
    }
}