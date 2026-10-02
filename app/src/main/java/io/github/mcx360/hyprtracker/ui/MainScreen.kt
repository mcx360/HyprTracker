package io.github.mcx360.hyprtracker.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.insightsScreen.GraphScreen
import io.github.mcx360.hyprtracker.ui.insightsScreen.InsightsViewModel
import io.github.mcx360.hyprtracker.ui.logsScreen.LogBPResult
import io.github.mcx360.hyprtracker.ui.logsScreen.LogsScreen
import io.github.mcx360.hyprtracker.ui.medicineScreen.AddMedicationScreen
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineScreen
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import io.github.mcx360.hyprtracker.ui.settingsScreen.Settings
import io.github.mcx360.hyprtracker.ui.theme.ThemeViewModel
import io.github.mcx360.hyprtracker.ui.utils.formatToDayMonthYear

enum class Destinations(@StringRes val title: Int) {
    //Main screen routes
    Logs(R.string.logging_screen_label),
    Medicine(R.string.medicine_screen_label),
    Insights(R.string.graph_screen_label),
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
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
    val openMenu = remember { mutableStateOf(false) }
    val openSettings = remember {mutableStateOf(false)}
    val openAddBPLog = remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val title = currentRoute ?: Destinations.Logs.name
    val openAddMedicationScreen = remember { mutableStateOf(false) }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            val insightsState = insightsViewModel.uiState.collectAsStateWithLifecycle()
            val logsState = hyprTrackerViewModel.uiState.collectAsStateWithLifecycle()
            TopAppBar(
                title = {
                    Column {
                        Text(text = title, style = MaterialTheme.typography.titleLarge)
                        if (title == Destinations.Logs.name && logsState.value.readings.isNotEmpty() || title == Destinations.Insights.name && insightsState.value.hasRecords){
                            Text(
                                text = "${formatToDayMonthYear(insightsState.value.startDate)}–${formatToDayMonthYear(insightsState.value.endDate)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } },
                actions = {
                    if (title == Destinations.Logs.name && logsState.value.readings.isNotEmpty()) {
                        IconButton(onClick = {}) { Icon(painter = painterResource(R.drawable.outline_filter_list_24), null) }
                    } else if (title == Destinations.Insights.name && insightsState.value.hasRecords){
                        IconButton(onClick = {}) { Icon(painter = painterResource(R.drawable.outline_filter_list_24), null) }
                    }
                    IconButton(onClick = { openMenu.value = !openMenu.value }) { Icon(Icons.Filled.MoreVert, null) }
                    when {
                        openMenu.value -> {
                            DropdownMenu(expanded = openMenu.value, onDismissRequest = {openMenu.value = false }) {
                                DropdownMenuItem(
                                    text = { Text(text = "Settings") },
                                    leadingIcon = { Icon(painter = painterResource(R.drawable.outline_settings_24), contentDescription = null) },
                                    onClick = {
                                        openSettings.value = true
                                        openMenu.value = false
                                    }
                                )
                            }
                        }
                    }
                }
            )
            when{
                openSettings.value -> Settings(
                    onDismissRequest = {openSettings.value = false},
                    hyprTrackerViewModel = hyprTrackerViewModel,
                    medicineViewModel = medicineViewModel,
                    themeViewModel = themeViewModel
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.inverseOnSurface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ) {
                NavigationBarItem(
                    selected = currentRoute == Destinations.Logs.name,
                    onClick = { navController.navigate(Destinations.Logs.name) },
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.outline_view_timeline_24), contentDescription = null) },
                    label = { Text(text = "Logs") },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(selectedTextColor = MaterialTheme.colorScheme.onSurface)
                )

                NavigationBarItem(
                    selected = currentRoute == Destinations.Medicine.name,
                    onClick = { navController.navigate(Destinations.Medicine.name) },
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_medicine), contentDescription = stringResource(R.string.medicine_screen_label)) },
                    label = { Text(text = stringResource(R.string.medicine_screen_label)) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(selectedTextColor = MaterialTheme.colorScheme.onSurface)
                )

                NavigationBarItem(
                    selected = currentRoute == Destinations.Insights.name,
                    onClick = { navController.navigate(Destinations.Insights.name) },
                    icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.ic_graph_insight), contentDescription = stringResource(R.string.graph_screen_label)) },
                    label = { Text(text = stringResource(R.string.graph_screen_label)) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(selectedTextColor = MaterialTheme.colorScheme.onSurface)
                )
            }
        },
        floatingActionButton = {
            if (title == Destinations.Logs.name){
                ExtendedFloatingActionButton(onClick = { openAddBPLog.value = true }) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Text("Add Reading", modifier = Modifier.padding(start = 8.dp))
                }
                when {
                    openAddBPLog.value -> {
                        LogBPResult(
                            onDismissRequest = { openAddBPLog.value = false },
                            hyprTrackerViewModel = hyprTrackerViewModel,
                            snackBarHostState = snackBarHostState,
                            scope = scope
                        )
                    }
                }
            }
            if (title == Destinations.Medicine.name){
                ExtendedFloatingActionButton(onClick = { openAddMedicationScreen.value = true }) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                    Text("Add Medicine", modifier = Modifier.padding(start = 8.dp))
                }
                when {
                    openAddMedicationScreen.value -> AddMedicationScreen(
                        onDismissRequest = {openAddMedicationScreen.value = false},
                        snackBarHostState = snackBarHostState,
                        scope = scope,
                        medicineViewModel = medicineViewModel
                    )
                }
            }
        }
    ) { innerpadding ->
        Box(modifier = Modifier.padding(innerpadding)) {
            key(currentRoute) {
                NavHost(navController = navController, startDestination = Destinations.Logs.name) {
                    composable(route = Destinations.Logs.name) { LogsScreen(hyprTrackerViewModel = hyprTrackerViewModel, snackbarHostState = snackBarHostState) }
                    composable(route = Destinations.Medicine.name) { MedicineScreen(medicineViewModel = medicineViewModel, snackbarHostState = snackBarHostState) }
                    composable(route = Destinations.Insights.name) { GraphScreen(insightsViewModel = insightsViewModel) }
                }
            }
        }
    }
}