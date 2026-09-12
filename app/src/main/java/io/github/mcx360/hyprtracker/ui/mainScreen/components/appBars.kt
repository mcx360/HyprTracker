package io.github.mcx360.hyprtracker.ui.mainScreen.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.insightsScreen.InsightsViewModel
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.Destinations
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import io.github.mcx360.hyprtracker.ui.utils.formatToDayMonthYear

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
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.outline_view_timeline_24),
                    contentDescription = null)
            },
            label = { Text(text = "Logs") },
            alwaysShowLabel = true,
            colors = colours
        )

        NavigationBarItem(
            selected = currentRoute == Destinations.Medicine.name,
            onClick = { navController.navigate(Destinations.Medicine.name) },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_medicine),
                    contentDescription = stringResource(R.string.medicine_screen_label))
            },
            label = { Text(text = stringResource(R.string.medicine_screen_label)) },
            alwaysShowLabel = true,
            colors = colours
        )

        NavigationBarItem(
            selected = currentRoute == Destinations.Insights.name,
            onClick = { navController.navigate(Destinations.Insights.name) },
            icon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_graph_insight),
                    contentDescription = stringResource(R.string.graph_screen_label))
            },
            label = { Text(text = stringResource(R.string.graph_screen_label)) },
            alwaysShowLabel = true,
            colors = colours
        )
    }
}

const val TOPAPPBAR_TAG = "topAppBar"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title: String?,
    updateOpenSettings: () -> Unit,
    updateOpenMenu: () -> Unit,
    insightsViewModel: InsightsViewModel,
    medicineViewModel: MedicineViewModel,
    hyprTrackerViewModel: HyprTrackerViewModel
){
    val bloodPressureState = hyprTrackerViewModel.uiState.collectAsStateWithLifecycle()
    val medicineState = medicineViewModel.uiState.collectAsStateWithLifecycle()
    val insightsState = insightsViewModel.uiState.collectAsStateWithLifecycle()
    val importer = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(), onResult = {uri -> })
    val exporter = rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("text/csv"), onResult = { uri -> })
    val context = LocalContext.current
    val openMenu = remember { mutableStateOf(false) }
    when (title) {
        null -> {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.app_name), style = MaterialTheme.typography.titleLarge) })
        }
        Destinations.Logs.name -> {
            TopAppBar(title = {
                Column{
                    Text("Logs",  style = MaterialTheme.typography.titleLarge)
                    Text("23 Aug 2026–30 Aug 2026", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                } }, actions = {
                Row() {
                    IconButton(onClick = {}) {
                        Icon(painter = painterResource(R.drawable.outline_filter_list_24),null)
                    }
                    IconButton(onClick = {
                        openMenu.value = !openMenu.value
                    }) { Icon(Icons.Filled.MoreVert, null) }
                    when{
                        openMenu.value -> {
                            LogScreenMenu(expanded = openMenu.value, onDismissRequest = {openMenu.value = false}, updateOpenSettings = {updateOpenSettings()})
                        }

                    }

                }
            })
        }
        Destinations.Medicine.name -> {
            TopAppBar(title = { Text(stringResource(R.string.medicine_screen_label),  style = MaterialTheme.typography.titleLarge) }, actions = {
                Box() {
                    IconButton(onClick = {
                        openMenu.value = !openMenu.value
                    }) { Icon(Icons.Filled.MoreVert, null) }
                    when{
                        openMenu.value -> {
                            smallMenu(expanded = openMenu.value, onDismissRequest = {openMenu.value = false}, updateOpenSettings = {updateOpenSettings()})
                        }

                    }
                }
            })
        }
        Destinations.Insights.name if !insightsState.value.hasRecords -> {
            TopAppBar(title = { Text(stringResource(R.string.graph_screen_label),  style = MaterialTheme.typography.titleLarge) }, actions = {
                Box {
                    IconButton(onClick = {
                        openMenu.value = !openMenu.value
                    }) { Icon(Icons.Filled.MoreVert, null) }
                    when{
                        openMenu.value -> {
                            smallMenu(expanded = openMenu.value, onDismissRequest = {openMenu.value = false}, updateOpenSettings = {updateOpenSettings()})
                        }

                    }
                }
            })
        }
        else -> {
            TopAppBar(
                modifier = modifier.testTag(TOPAPPBAR_TAG).padding(end = 8.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Column {
                        if (title == Destinations.Insights.name) Column {
                            Text(text = title); Text(
                            text = "${
                                formatToDayMonthYear(
                                    insightsState.value.startDate
                                )
                            }–${formatToDayMonthYear(insightsState.value.endDate)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        } else title?.let { Text(text = it) }
                    }
                },
                actions = {
                    when (title) {
                        stringResource(R.string.History_tab) -> {
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Hi, here are my blood pressure readings from the past week:"
                                    )
                                }
                                val chooser = Intent.createChooser(intent, "Share via")
                                context.startActivity(chooser)
                            }) {
                                Icon(painter = painterResource(R.drawable.baseline_share_24), null)
                            }
                            IconButton(onClick = { exporter.launch("logs.csv") }) {
                                Icon(Icons.Filled.MoreVert, null)
                            }
                        }

                        Destinations.Insights.name -> {
                            IconButton(onClick = {}) {
                                Icon(painter = painterResource(R.drawable.outline_filter_list_24), null)
                            }
                        }

                        else -> {

                        }
                    }
                }
            )
        }
    }
}