package io.github.mcx360.hyprtracker.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.Destinations
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.NavHostContainer
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.mainScreen.components.AboutDialog
import io.github.mcx360.hyprtracker.ui.mainScreen.components.BugReportDialog
import io.github.mcx360.hyprtracker.ui.mainScreen.components.HyprTrackerBottomNavigationBar
import io.github.mcx360.hyprtracker.ui.mainScreen.components.HyprTrackerDrawerContent
import io.github.mcx360.hyprtracker.ui.mainScreen.components.HyprTrackerTopAppBar
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import kotlinx.coroutines.launch

const val TOPAPPBAR_TAG = "topAppBar"
const val BOTTOMNAVBAR_TAG = "bottomNavBar"
const val NAVIGATIONDRAWER_TAG = "navigationDrawer"
const val EXPORT_LOGS_IN_NAVIGATIONDRAWER_TAG = "exportLogs"
const val SHARE_LOGS_IN_NAVIGATIONDRAWER_TAG = "share logs"
const val MY_DOCUMENTS_IN_NAVIGATIONDRAWER_TAG = "myDocuments"
const val BIN_IN_NAVIGATIONDRAWER_TAG = "bin"
const val BACKUP_IN_NAVIGATIONDRAWER_TAG = "backup"
const val RATE_APP_IN_NAVIGATIONDRAWER_TAG = "rateApp"
const val REPORT_BUG_IN_NAVIGATIONDRAWER_TAG = "reportBug"
const val USERS_AND_SETTINGS_IN_NAVIGATIONDRAWER_TAG = "settingsAndUsers"
const val ABOUT_IN_NAVIGATIONDRAWER_TAG = "about"

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HyprTrackerScreen(
    modifier: Modifier = Modifier,
    hyprTrackerViewModel: HyprTrackerViewModel = viewModel(factory = HyprTrackerViewModel.Factory) ,
    medicineViewModel: MedicineViewModel = viewModel(factory = MedicineViewModel.Factory)
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val openAboutDialog = remember { mutableStateOf(false) }
    val openBugReportDialog = remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val openAddMedicationScreen = remember { mutableStateOf(false) }

    Box(modifier = modifier
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .statusBarsPadding()
    ){
    ModalNavigationDrawer(
        modifier = modifier.testTag(NAVIGATIONDRAWER_TAG),
        drawerContent = {
            HyprTrackerDrawerContent(
                scope = scope,
                drawerState = drawerState,
                updateOpenBugReportDialogToTrue = {openBugReportDialog.value = true},
                updateOpenAboutDialogToTrue = {openAboutDialog.value = true},
                snackbarHostState = snackBarHostState
            )
        },
        drawerState = drawerState
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                HyprTrackerTopAppBar(drawerState = drawerState, scope = scope, title = currentRoute)
            },
            bottomBar = {
                HyprTrackerBottomNavigationBar(
                    currentRoute = currentRoute,
                    navController = navController,
                    modifier = Modifier
                )
            },
            floatingActionButton = {
                if (currentRoute == Destinations.Medicines.name && !openAddMedicationScreen.value) {
                    FloatingActionButton(
                        onClick = {openAddMedicationScreen.value = !openAddMedicationScreen.value},
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) { innerpadding ->
            Box(modifier = Modifier.padding(innerpadding)) {
                key(currentRoute) {
                NavHostContainer(navController = navController, hyprTrackerViewModel, snackBarHostState, openAddMedicationScreen, medicineViewModel)
            }
                when{
                    openAboutDialog.value -> {
                        AboutDialog(
                            onDismissRequest = {
                                openAboutDialog.value = false
                                scope.launch {
                                    drawerState.apply {
                                        if (isOpen) close() else open()                                    }
                                }
                            },
                            modifier = modifier
                        )
                    }
                }
                when{
                    openBugReportDialog.value -> {
                        BugReportDialog(
                            onDismissRequest = {
                                openBugReportDialog.value = false
                                scope.launch {
                                    drawerState.apply {
                                        if (isOpen) close() else open()
                                    }
                                }
                            },
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
    }
}