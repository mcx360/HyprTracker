package io.github.mcx360.hyprtracker.ui.mainScreen.components.appBars

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.Destinations

const val BOTTOMNAVBAR_TAG = "bottomNavBar"


@Composable
fun HyprTrackerBottomNavigationBar(
    navController: NavHostController,
    currentRoute: String?,
    modifier: Modifier
){
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.testTag(BOTTOMNAVBAR_TAG)
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
            alwaysShowLabel = true
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
        )

    }
}