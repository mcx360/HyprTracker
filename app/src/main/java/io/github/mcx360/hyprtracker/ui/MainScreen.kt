package io.github.mcx360.hyprtracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.navigation.Destinations
import io.github.mcx360.hyprtracker.navigation.NavHostContainer
import io.github.mcx360.hyprtracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val TOPAPPBAR_TAG = "topAppBar"
const val BOTTOMNAVBAR_TAG = "bottomNavBar"
const val NAVIGATIONDRAWER_TAG = "navigationDrawer"
const val EXPORTLOGS_IN_NAVIGATIONDRAWER_TAG = "exportLogs"
const val SHARELOGS_IN_NAVIGATIONDRAWER_TAG = "share logs"
const val MY_DOCUMENTS_IN_NAVIGATIONDRAWER_TAG = "myDocuments"
const val BIN_IN_NAVIGATIONDRAWER_TAG = "bin"
const val BACKUP_IN_NAVIGATIONDRAWER_TAG = "backup"
const val RATEAPP_IN_NAVIGATIONDRAWER_TAG = "rateApp"
const val REPORTBUG_IN_NAVIGATIONDRAWER_TAG = "reportBug"
const val USERSANDSETTINGS_IN_NAVIGATIONDRAWER_TAG = "settingsAndUsers"
const val ABOUT_IN_NAVIGATIONDRAWER_TAG = "about"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HyprTrackerTopAppBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    title: String?,
    modifier: Modifier = Modifier,
){
    CenterAlignedTopAppBar(
        modifier = Modifier.testTag(TOPAPPBAR_TAG),
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
fun HyprTrackerBottomNavigationBar(
    navController: NavHostController,
    navBackStack: NavBackStackEntry?,
    currentRoute: String?,
    modifier: Modifier
){
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.testTag(BOTTOMNAVBAR_TAG)
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
fun AboutDialog(onDismissRequest: () -> Unit, ){
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text("About dialog text")
                Button(onClick = {
                    onDismissRequest()

                }) {
                    Text("Okay")
                }
            }

        }
    }
}

@Composable
fun BugReportDialog(onDismissRequest: () -> Unit){
    Dialog(onDismissRequest = {}){
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Please Describe bug report")
                TextField(
                    value = "toDO",
                    onValueChange = {},
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = {
                    onDismissRequest()
                }) {
                    Text("Confirm")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HyprTrackerScreen(modifier: Modifier = Modifier, hyprTrackerViewModel: HyprTrackerViewModel = viewModel()){

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val openAboutDialog = remember { mutableStateOf(false) }
    val openBugReportDialog = remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    Box(modifier = Modifier
        .background(color = MaterialTheme.colorScheme.primaryContainer)
        .statusBarsPadding()
    ){
    ModalNavigationDrawer(
        modifier = Modifier.testTag(NAVIGATIONDRAWER_TAG),
        drawerContent = {
                ModalDrawerSheet{
                    Text(stringResource(R.string.app_name), modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(all = 32.dp),
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text(text= stringResource(R.string.export_label)) },
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_export), contentDescription = null)
                        },
                        modifier = Modifier.testTag(EXPORTLOGS_IN_NAVIGATIONDRAWER_TAG)

                    )
                    NavigationDrawerItem(
                        label = { Text(text= stringResource(R.string.share_label)) },
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_share), contentDescription = null)
                        },
                        modifier = Modifier.testTag(SHARELOGS_IN_NAVIGATIONDRAWER_TAG)
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.documents_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_document), contentDescription = null)
                        },
                        modifier = Modifier.testTag(MY_DOCUMENTS_IN_NAVIGATIONDRAWER_TAG)
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.bin_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_bin), contentDescription = null)
                        },
                        modifier = Modifier.testTag(BIN_IN_NAVIGATIONDRAWER_TAG)
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.backup_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_restore), contentDescription = null)
                        },
                        modifier = Modifier.testTag(BACKUP_IN_NAVIGATIONDRAWER_TAG)
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.rating_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_rate), contentDescription = null)
                        },
                        modifier = Modifier.testTag(RATEAPP_IN_NAVIGATIONDRAWER_TAG)
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.bug_report_label))},
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isOpen) close() else open()
                                }
                            }
                            openBugReportDialog.value = true
                        },
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_bug_report), contentDescription = null)
                        },
                        modifier = Modifier.testTag(REPORTBUG_IN_NAVIGATIONDRAWER_TAG)
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.settings_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_settings_and_users), contentDescription = null)
                        },
                        modifier = Modifier.testTag(USERSANDSETTINGS_IN_NAVIGATIONDRAWER_TAG)
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.about_label))},
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isOpen) close() else open()                                    }
                            }
                            openAboutDialog.value = true
                        },
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_about), contentDescription = null)
                        },
                        modifier = Modifier.testTag(ABOUT_IN_NAVIGATIONDRAWER_TAG)
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
                HyprTrackerBottomNavigationBar(
                    currentRoute = currentRoute,
                    navController = navController,
                    navBackStack = navBackStackEntry,
                    modifier = Modifier
                )
            },
            floatingActionButton = {
                if (currentRoute == Destinations.MEDICINESCREEN.name) {
                    FloatingActionButton(
                        onClick = {},
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) { innerpadding ->
            Box(
                modifier = Modifier
                    .padding(innerpadding)
            ) {
                NavHostContainer(navController = navController, hyprTrackerViewModel, snackBarHostState)
                when{
                    openAboutDialog.value -> {
                        AboutDialog(
                            onDismissRequest = {
                                openAboutDialog.value = false
                                scope.launch {
                                    drawerState.apply {
                                        if (isOpen) close() else open()                                    }
                                }
                            }
                        )
                    }
                }
                when{
                    openBugReportDialog.value -> {
                        BugReportDialog(
                            onDismissRequest = {
                                openBugReportDialog.value = false
                                scope.launch { drawerState.apply {
                                    if (isOpen) close() else open()
                                } }
                            }
                        )
                    }
                }
            }
        }
    }
    }
}