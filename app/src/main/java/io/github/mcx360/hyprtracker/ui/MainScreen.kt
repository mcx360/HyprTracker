package io.github.mcx360.hyprtracker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.utils.Destinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HyprTrackerTopAppBar(scope: CoroutineScope, drawerState: DrawerState, title: String?){
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (title == null) Text(stringResource(R.string.app_name)) else Text(text = title.lowercase())
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
        },
        actions = {
            IconButton(
                onClick = {},
                modifier = Modifier.padding(horizontal = 16.dp),
            ){
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun HyprTrackerBottomNavigationBar(navController: NavHostController, navBackStack: NavBackStackEntry?, currentRoute: String?){
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary
    ) {
        NavigationBarItem(
            selected = currentRoute == Destinations.LOGGINGSCREEN.name,
            onClick = {
                navController.navigate(Destinations.LOGGINGSCREEN.name)
            },
            icon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_log_blood_pressure),
                    contentDescription = stringResource(R.string.logging_screen_label))
            },
            label = {
                Text(text = stringResource(R.string.logging_screen_label))
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = currentRoute == Destinations.MEDICINESCREEN.name,
            onClick = {
                navController.navigate(Destinations.MEDICINESCREEN.name)
            },
            icon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_medicine),
                    contentDescription = stringResource(R.string.medicine_screen_label))
            },
            label = {
                Text(text = stringResource(R.string.medicine_screen_label))
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = currentRoute == Destinations.GRAPHVIEWSCREEN.name,
            onClick = {
                navController.navigate(Destinations.GRAPHVIEWSCREEN.name)
            },
            icon = {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_graph_insight),
                    contentDescription = stringResource(R.string.graph_screen_label))
            },
            label = {
                Text(text = stringResource(R.string.graph_screen_label))
            },
            alwaysShowLabel = true,
        )
    }
}

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

@Composable
fun HyprTrackerScreen(modifier: Modifier = Modifier){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerContent = {
                ModalDrawerSheet{
                    Text(stringResource(R.string.app_name), modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 32.dp),
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text= stringResource(R.string.export_label)) },
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_export), contentDescription = null)
                        }

                    )
                    NavigationDrawerItem(
                        label = { Text(text= stringResource(R.string.share_label)) },
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_share), contentDescription = null)
                        }
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.documents_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_document), contentDescription = null)
                        }
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.bin_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_bin), contentDescription = null)
                        }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.backup_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_restore), contentDescription = null)
                        }
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.rating_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_rate), contentDescription = null)
                        }
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.bug_report_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_bug_report), contentDescription = null)
                        }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.settings_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_settings_and_users), contentDescription = null)
                        }
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.about_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_about), contentDescription = null)
                        }
                    )
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                HyprTrackerTopAppBar(drawerState = drawerState, scope = scope, title = currentRoute)
            },
            bottomBar = {
                HyprTrackerBottomNavigationBar(currentRoute = currentRoute, navController = navController, navBackStack = navBackStackEntry)
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {},
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                }
            }
        ) { innerpadding ->
            Box(modifier = Modifier.padding(innerpadding)){
                NavHostContainer(navController = navController)
            }
        }

    }
}