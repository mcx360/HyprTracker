package io.github.mcx360.hyprtracker.ui.mainScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.insightsScreen.InsightsViewModel
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.Destinations
import io.github.mcx360.hyprtracker.ui.utils.formatToDayMonthYear

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
                    Column{
                        Text(
                            text = "Logs",
                            style = MaterialTheme.typography.titleLarge
                        )
                        if (logsState.value.readings.isNotEmpty()) {
                            Text(
                                text = "${formatToDayMonthYear(insightsState.value.startDate)}–${formatToDayMonthYear(insightsState.value.endDate)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    if (logsState.value.readings.isNotEmpty()){
                        IconButton(onClick = {}) {
                            Icon(painter = painterResource(R.drawable.outline_filter_list_24),null)
                        }
                    }
                    IconButton(onClick = { openMenu.value = !openMenu.value }) {
                        Icon(Icons.Filled.MoreVert, null)
                    }
                    when{
                        openMenu.value -> {
                            SmallMenu(expanded = openMenu.value, onDismissRequest = {openMenu.value = false}, updateOpenSettings = {updateOpenSettings()})                        }
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