package io.github.mcx360.hyprtracker.ui.mainScreen.components

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.mcx360.hyprtracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val EXPORT_LOGS_IN_NAVIGATIONDRAWER_TAG = "exportLogs"
const val SHARE_LOGS_IN_NAVIGATIONDRAWER_TAG = "share logs"
const val BIN_IN_NAVIGATIONDRAWER_TAG = "bin"
const val BACKUP_IN_NAVIGATIONDRAWER_TAG = "backup"
const val RATE_APP_IN_NAVIGATIONDRAWER_TAG = "rateApp"
const val REPORT_BUG_IN_NAVIGATIONDRAWER_TAG = "reportBug"
const val USERS_AND_SETTINGS_IN_NAVIGATIONDRAWER_TAG = "settingsAndUsers"
const val ABOUT_IN_NAVIGATIONDRAWER_TAG = "about"


@Composable
fun HyprTrackerDrawerContent(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    drawerState: DrawerState,
    updateOpenBugReportDialogToTrue: () -> Unit,
    updateOpenAboutDialogToTrue: () -> Unit,
    snackbarHostState: SnackbarHostState,
    updateOpenSettingsDialogToTrue: () -> Unit
){
    val context = LocalContext.current
    val exporter = rememberLauncherForActivityResult(
        contract = CreateDocument("text/csv"),
        onResult = { uri -> }
    )

        val importer = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = {uri -> }
    )

        ModalDrawerSheet{
            //Title card
            Card(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.app_name),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            HorizontalDivider()

            //My Data header
            Text(
                text = stringResource(R.string.title_one_label),
                modifier = modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )

            //Export logs
            NavigationDrawerItem(
                label = { Text(text= stringResource(R.string.export_label)) },
                selected = false,
                onClick = {
                    exporter.launch("logs.csv")
                    scope.launch {
                        drawerState.apply { if (isOpen) close() else open() }
                    } },
                icon = { Icon(painter = painterResource(R.drawable.ic_export), contentDescription = null) },
                modifier = modifier.testTag(EXPORT_LOGS_IN_NAVIGATIONDRAWER_TAG)
            )

            //Share logs
            NavigationDrawerItem(
                label = { Text(text= stringResource(R.string.share_label)) },
                selected = false,
                onClick = {
                    scope.launch {
                        drawerState.apply { if (isOpen) close() else open() }
                    }
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "text")
                    }
                    val chooser = Intent.createChooser(intent, "Share via")
                    context.startActivity(chooser)
                },
                icon = { Icon(painter = painterResource(R.drawable.ic_share), contentDescription = null) },
                modifier = modifier.testTag(SHARE_LOGS_IN_NAVIGATIONDRAWER_TAG)
            )

            //bin
            NavigationDrawerItem(
                label = {Text(text = stringResource(R.string.bin_label))},
                selected = false,
                onClick = {
                    scope.launch {
                        drawerState.apply { if (isOpen) close() else open() } }
                },
                icon = { Icon(painter = painterResource(R.drawable.ic_bin), contentDescription = null) },
                modifier = modifier.testTag(BIN_IN_NAVIGATIONDRAWER_TAG)
            )

            HorizontalDivider()

            // External header
            Text(
                text = stringResource(R.string.title_two_label),
                modifier = modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )

            //Import logs
            NavigationDrawerItem(
                label = {Text(text = stringResource(R.string.backup_label))},
                selected = false,
                onClick = {
                    importer.launch("text/csv")
                    scope.launch {
                        drawerState.apply { if (isOpen) close() else open() }
                    }
                },
                icon = { Icon(painter = painterResource(R.drawable.ic_restore), contentDescription = null) },
                modifier = modifier.testTag(BACKUP_IN_NAVIGATIONDRAWER_TAG)
            )

            //Rate app
            NavigationDrawerItem(
                label = {Text(text = stringResource(R.string.rating_label))},
                selected = false,
                onClick = {
                    scope.launch {
                        drawerState.apply { if (isOpen) close() else open() }
                        snackbarHostState.showSnackbar("Cannot rate app yet as app has not been released yet")
                    }
                },
                icon = { Icon(painter = painterResource(R.drawable.ic_rate), contentDescription = null) },
                modifier = modifier.testTag(RATE_APP_IN_NAVIGATIONDRAWER_TAG)
            )

            //Report bug
            NavigationDrawerItem(
                label = {Text(text = stringResource(R.string.bug_report_label))},
                selected = false,
                onClick = {
                    scope.launch {
                        drawerState.apply { if (isOpen) close() else open() }
                    }
                    updateOpenBugReportDialogToTrue()
                },
                icon = { Icon(painter = painterResource(R.drawable.ic_bug_report), contentDescription = null) },
                modifier = modifier.testTag(REPORT_BUG_IN_NAVIGATIONDRAWER_TAG)
            )

            HorizontalDivider()

            //Settings header
            Text(
                text = stringResource(R.string.title_three_label),
                modifier = modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )

            //Settings
            NavigationDrawerItem(
                label = {Text(text = stringResource(R.string.settings_label))},
                selected = false,
                onClick = {updateOpenSettingsDialogToTrue()
                    scope.launch {
                        drawerState.apply { if (isOpen) close() else open() }
                    }},
                icon = { Icon(painter = painterResource(R.drawable.outline_settings_24), contentDescription = null) },
                modifier = modifier.testTag(USERS_AND_SETTINGS_IN_NAVIGATIONDRAWER_TAG)
            )

            //About
            NavigationDrawerItem(
                label = {Text(text = stringResource(R.string.about_label))},
                selected = false,
                onClick = {
                    scope.launch {
                        drawerState.apply { if (isOpen) close() else open()} }
                    updateOpenAboutDialogToTrue()
                },
                icon = { Icon(painter = painterResource(R.drawable.ic_about), contentDescription = null) },
                modifier = modifier.testTag(ABOUT_IN_NAVIGATIONDRAWER_TAG)
            )

            HorizontalDivider()

            //Version number
            Text(
                text = "HyprTracker 0.5.0",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
        }
}