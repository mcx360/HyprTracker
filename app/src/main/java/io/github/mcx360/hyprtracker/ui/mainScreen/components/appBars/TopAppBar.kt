package io.github.mcx360.hyprtracker.ui.mainScreen.components.appBars

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.mainScreen.navigation.Destinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val TOPAPPBAR_TAG = "topAppBar"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HyprTrackerTopAppBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    title: String?,
    modifier: Modifier = Modifier,
    updateOpenSettings: () -> Unit
){
    val importer = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri -> }
    )

    val context = LocalContext.current


    val exporter = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri -> }
    )

    TopAppBar(
         modifier = modifier.testTag(TOPAPPBAR_TAG).padding(end = 8.dp),
         colors = TopAppBarDefaults.topAppBarColors(
             containerColor = MaterialTheme.colorScheme.surface,
             titleContentColor = MaterialTheme.colorScheme.onSurface,
         ),
         title = { if (title == null) Text(stringResource(R.string.app_name)) else Text(text = title) },
        actions = {
            when (title) {
                stringResource(R.string.logging_screen_label) -> {
                    IconButton(onClick = {updateOpenSettings()}) {
                        Icon(painter = painterResource(R.drawable.outline_settings_24),null)
                    }
                    IconButton(onClick = {importer.launch("text/csv")}) {
                        Icon(painter = painterResource(R.drawable.outline_file_open_24), null)
                    }
                }
                stringResource(R.string.History_tab) -> {
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "Hi, here are my blood pressure readings from the past week:")
                        }
                        val chooser = Intent.createChooser(intent, "Share via")
                        context.startActivity(chooser)
                    }) {
                        Icon(painter = painterResource(R.drawable.baseline_share_24), null)
                    }
                    IconButton(onClick = {exporter.launch("logs.csv") }) {
                        Icon(painter = painterResource(R.drawable.outline_file_export_24), null)
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