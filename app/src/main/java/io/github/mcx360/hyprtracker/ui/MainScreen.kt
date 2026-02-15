package io.github.mcx360.hyprtracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.utils.Constants
import io.github.mcx360.hyprtracker.utils.Destinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HyprTrackerTopAppBar(scope: CoroutineScope, drawerState: DrawerState){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.apply {
                        if (isClosed) open() else close()
                    }
                }
            }) {
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
fun HyprTrackerModalNavigationDrawer(modifier: Modifier = Modifier, drawerState: DrawerState) {
    val drawerState = drawerState
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet{
                Text("HyprTracker", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text= "Drawer Item") },
                    selected = false,
                    onClick = {/*To be implemented*/}
                )
            }
        }
    ){

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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        modifier = modifier,
        topBar = {
            HyprTrackerTopAppBar(drawerState = drawerState, scope = scope)
        },
        bottomBar = {
            HyprTrackerBottomNavigationBar(navController = navController)
        }
    ) { innerpadding ->
        Box(modifier = Modifier.padding(innerpadding)){
            NavHostContainer(navController = navController)
            HyprTrackerModalNavigationDrawer(drawerState = drawerState)
        }
    }
}