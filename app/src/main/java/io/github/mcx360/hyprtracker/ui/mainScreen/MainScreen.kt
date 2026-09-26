package io.github.mcx360.hyprtracker.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.insightsScreen.GraphScreen
import io.github.mcx360.hyprtracker.ui.insightsScreen.InsightsViewModel
import io.github.mcx360.hyprtracker.ui.logsScreen.LogsScreen
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.Settings
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.ThemeViewModel
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineScreen
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import io.github.mcx360.hyprtracker.ui.utils.formatToDayMonthYear

enum class Destinations {
    //Main screen routes
    Logs,
    Medicine,
    Insights,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: String?,
    updateOpenSettings: () -> Unit,
    insightsViewModel: InsightsViewModel,
    hyprTrackerViewModel: HyprTrackerViewModel
){
    val insightsState = insightsViewModel.uiState.collectAsStateWithLifecycle()
    val logsState = hyprTrackerViewModel.uiState.collectAsStateWithLifecycle()
    val openMenu = remember { mutableStateOf(false) }
    when (title) {
        Destinations.Logs.name -> {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Logs",
                            style = MaterialTheme.typography.titleLarge
                        )
                        if (logsState.value.readings.isNotEmpty()) {
                            Text(
                                text = "${formatToDayMonthYear(insightsState.value.startDate)}–${
                                    formatToDayMonthYear(
                                        insightsState.value.endDate
                                    )
                                }",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    if (logsState.value.readings.isNotEmpty()) {
                        IconButton(onClick = {}) {
                            Icon(painter = painterResource(R.drawable.outline_filter_list_24), null)
                        }
                    }
                    IconButton(onClick = { openMenu.value = !openMenu.value }) {
                        Icon(Icons.Filled.MoreVert, null)
                    }
                    when {
                        openMenu.value -> {
                            SmallMenu(
                                expanded = openMenu.value,
                                onDismissRequest = { openMenu.value = false },
                                updateOpenSettings = { updateOpenSettings() })
                        }
                    }
                }
            )
        }

        Destinations.Medicine.name -> {
            TopAppBar(
                title = {
                    Text(
                        text =  stringResource(R.string.medicine_screen_label),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    Box {
                        IconButton(onClick = { openMenu.value = !openMenu.value }) {
                            Icon(Icons.Filled.MoreVert, null)
                        }
                        when{
                            openMenu.value -> {
                                SmallMenu(expanded = openMenu.value, onDismissRequest = {openMenu.value = false}, updateOpenSettings = {updateOpenSettings()})
                            }
                        }
                    }
                }
            )
        }

        Destinations.Insights.name-> {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.graph_screen_label),
                            style = MaterialTheme.typography.titleLarge
                        )
                        if (insightsState.value.hasRecords){
                            Text(
                                text = "${formatToDayMonthYear(insightsState.value.startDate)}–${formatToDayMonthYear(insightsState.value.endDate)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    if (insightsState.value.hasRecords) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(R.drawable.outline_filter_list_24),
                                null
                            )
                        }
                    }
                    Box {
                        IconButton(onClick = { openMenu.value = !openMenu.value }) {
                            Icon(Icons.Filled.MoreVert, null)
                        }
                        when {
                            openMenu.value -> {
                                SmallMenu(
                                    expanded = openMenu.value,
                                    onDismissRequest = { openMenu.value = false },
                                    updateOpenSettings = { updateOpenSettings() })
                            }
                        }
                    }
                }
            )
        }

        else -> {}
    }
}

@Composable
fun BottomNavBar(
    navController: NavHostController,
    currentRoute: String?,
){
    val colours = NavigationBarItemColors(
        selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
        selectedTextColor = MaterialTheme.colorScheme.onSurface,
        selectedIndicatorColor = MaterialTheme.colorScheme.secondaryContainer,
        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
        disabledIconColor = MaterialTheme.colorScheme.error,
        disabledTextColor = MaterialTheme.colorScheme.error
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        NavigationBarItem(
            selected = currentRoute == Destinations.Logs.name,
            onClick = { navController.navigate(Destinations.Logs.name) },
            icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.outline_view_timeline_24), contentDescription = null)},
            label = { Text(text = "Logs") },
            alwaysShowLabel = true,
            colors = colours
        )

        NavigationBarItem(
            selected = currentRoute == Destinations.Medicine.name,
            onClick = { navController.navigate(Destinations.Medicine.name) },
            icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_medicine), contentDescription = stringResource(R.string.medicine_screen_label)) },
            label = { Text(text = stringResource(R.string.medicine_screen_label)) },
            alwaysShowLabel = true,
            colors = colours
        )

        NavigationBarItem(
            selected = currentRoute == Destinations.Insights.name,
            onClick = { navController.navigate(Destinations.Insights.name) },
            icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_graph_insight), contentDescription = stringResource(R.string.graph_screen_label)) },
            label = { Text(text = stringResource(R.string.graph_screen_label)) },
            alwaysShowLabel = true,
            colors = colours
        )
    }
}

@Composable
fun SmallMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    updateOpenSettings: () -> Unit
){
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {onDismissRequest()}
    ) {
        DropdownMenuItem(
            text = { Text(text = "Settings") },
            leadingIcon = {Icon(painterResource(R.drawable.outline_settings_24), contentDescription =null)},
            onClick = {
                updateOpenSettings()
                onDismissRequest()
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HyprTrackerApp(
    hyprTrackerViewModel: HyprTrackerViewModel = viewModel(factory = HyprTrackerViewModel.Factory),
    medicineViewModel: MedicineViewModel = viewModel(factory = MedicineViewModel.Factory),
    themeViewModel: ThemeViewModel = viewModel(factory = ThemeViewModel.Factory),
    insightsViewModel: InsightsViewModel = viewModel(factory = InsightsViewModel.Factory)
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val snackBarHostState = remember { SnackbarHostState() }
    val openAddMedicationScreen = remember { mutableStateOf(false) }
    val openSettingsDialog = remember { mutableStateOf(false) }
    val openAddBPLog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
    ) {
        Scaffold(
            modifier = Modifier,
            topBar = {
                TopAppBar(
                    title = currentRoute,
                    updateOpenSettings = {openSettingsDialog.value = true},
                    insightsViewModel = insightsViewModel,
                    hyprTrackerViewModel = hyprTrackerViewModel
                )
            },
            bottomBar = {
                BottomNavBar(
                    currentRoute = currentRoute,
                    navController = navController,
                )
            },
            floatingActionButton = {
                if (currentRoute == Destinations.Medicine.name) {
                    FloatingActionButton(onClick = { openAddMedicationScreen.value = true}) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }else if (currentRoute == Destinations.Logs.name){
                    FloatingActionButton(onClick = { openAddBPLog.value = true }) {
                        Icon(Icons.Filled.Edit, contentDescription = null)
                    }
                }
            },
            snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
        ) { innerpadding ->
            Box(modifier = Modifier.padding(innerpadding)) {
                key(currentRoute) {
                    NavHost(
                        navController = navController,
                        startDestination = Destinations.Logs.name
                    ){
                        composable(route = Destinations.Logs.name){
                            LogsScreen(hyprTrackerViewModel = hyprTrackerViewModel, snackBarHostState = snackBarHostState, openAddBloodPressureLog = openAddBPLog)
                        }
                        composable(route = Destinations.Medicine.name){
                            MedicineScreen(openAddMedicationScreen = openAddMedicationScreen, snackBarHostState = snackBarHostState, medicineViewModel = medicineViewModel)
                        }
                        composable(route = Destinations.Insights.name){
                            GraphScreen(insightsViewModel = insightsViewModel)
                        }
                    }
                }

                when {
                    openSettingsDialog.value -> {
                        Settings(
                            onDismissRequest = { openSettingsDialog.value = false },
                            hyprTrackerViewModel = hyprTrackerViewModel,
                            medicineViewModel = medicineViewModel,
                            themeViewModel = themeViewModel
                        )
                    }
                }
            }
        }
    }
}