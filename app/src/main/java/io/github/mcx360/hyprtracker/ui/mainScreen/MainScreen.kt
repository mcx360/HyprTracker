package io.github.mcx360.hyprtracker.ui.mainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.runtime.key
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.Destinations
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.NavHostContainer
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import kotlinx.coroutines.CoroutineScope
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HyprTrackerTopAppBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    title: String?,
    modifier: Modifier = Modifier,
){
    CenterAlignedTopAppBar(
        modifier = modifier.testTag(TOPAPPBAR_TAG),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            if (title == null) Text(stringResource(R.string.app_name)) else Text(text = title)
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
    )
}

@Composable
fun HyprTrackerBottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?,
    modifier: Modifier
){
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier.testTag(BOTTOMNAVBAR_TAG)
    ) {
        NavigationBarItem(
            selected = currentRoute == Destinations.Logging.name,
            onClick = {
                navController.navigate(Destinations.Logging.name)
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
            selected = currentRoute == Destinations.Medicines.name,
            onClick = {
                navController.navigate(Destinations.Medicines.name)
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
            selected = currentRoute == Destinations.Insight.name,
            onClick = {
                navController.navigate(Destinations.Insight.name)
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
fun AboutDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {

    Dialog(onDismissRequest = onDismissRequest) {

        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "About",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "HyprTrek is an app designed to help you keep a journal of your blood pressure readings.",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Contact",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Email: email@email.com",
                    modifier = modifier.padding(top = 4.dp)
                )

                Spacer(modifier = modifier.weight(1f))

                Button(
                    onClick = onDismissRequest,
                    modifier = modifier.padding(bottom = 16.dp)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

//Note to self: Report bug functionality will have a link to a page where you can report the bug so that the app itself does not make any internet connection as this is a strictly offline app
@Composable
fun BugReportDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier
){
    Dialog(onDismissRequest = {}){
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Please Describe bug report")
                TextField(
                    value = "toDO",
                    onValueChange = {},
                    modifier = modifier.padding(16.dp)
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
fun HyprTrackerScreen(
    modifier: Modifier = Modifier,
    hyprTrackerViewModel: HyprTrackerViewModel = viewModel(factory = HyprTrackerViewModel.Factory)
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
                ModalDrawerSheet{
                    Card(modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer).padding(32.dp).fillMaxWidth()) {
                        Text(stringResource(R.string.app_name),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = modifier.background(MaterialTheme.colorScheme.primaryContainer).fillMaxWidth(),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    HorizontalDivider()
                    Text(stringResource(R.string.title_one_label), modifier = modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    NavigationDrawerItem(
                        label = { Text(text= stringResource(R.string.export_label)) },
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_export), contentDescription = null)
                        },
                        modifier = modifier.testTag(EXPORT_LOGS_IN_NAVIGATIONDRAWER_TAG)

                    )
                    NavigationDrawerItem(
                        label = { Text(text= stringResource(R.string.share_label)) },
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_share), contentDescription = null)
                        },
                        modifier = modifier.testTag(SHARE_LOGS_IN_NAVIGATIONDRAWER_TAG)
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.documents_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_document), contentDescription = null)
                        },
                        modifier = modifier.testTag(MY_DOCUMENTS_IN_NAVIGATIONDRAWER_TAG)
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.bin_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_bin), contentDescription = null)
                        },
                        modifier = modifier.testTag(BIN_IN_NAVIGATIONDRAWER_TAG)
                    )
                    HorizontalDivider()
                    Text(stringResource(R.string.title_two_label), modifier = modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.backup_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_restore), contentDescription = null)
                        },
                        modifier = modifier.testTag(BACKUP_IN_NAVIGATIONDRAWER_TAG)
                    )
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.rating_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_rate), contentDescription = null)
                        },
                        modifier = modifier.testTag(RATE_APP_IN_NAVIGATIONDRAWER_TAG)
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
                        modifier = modifier.testTag(REPORT_BUG_IN_NAVIGATIONDRAWER_TAG)
                    )
                    HorizontalDivider()
                    Text(stringResource(R.string.title_three_label), modifier = modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                    NavigationDrawerItem(
                        label = {Text(text = stringResource(R.string.settings_label))},
                        selected = false,
                        onClick = {},
                        icon = {
                            Icon(painter = painterResource(R.drawable.ic_settings_and_users), contentDescription = null)
                        },
                        modifier = modifier.testTag(USERS_AND_SETTINGS_IN_NAVIGATIONDRAWER_TAG)
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
                        modifier = modifier.testTag(ABOUT_IN_NAVIGATIONDRAWER_TAG)
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
            Box(
                modifier = Modifier
                    .padding(innerpadding)
            ) { key(currentRoute) {
                NavHostContainer(navController = navController, hyprTrackerViewModel, snackBarHostState, openAddMedicationScreen)
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