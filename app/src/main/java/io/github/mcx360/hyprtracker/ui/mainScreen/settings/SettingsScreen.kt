package io.github.mcx360.hyprtracker.ui.mainScreen.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.components.AboutDialog
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.components.BugReportDialog
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.components.Picker
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import io.github.mcx360.hyprtracker.ui.theme.ThemeMode
import io.github.mcx360.hyprtracker.ui.utils.DeletionDialog
import io.github.mcx360.hyprtracker.ui.utils.TitleBarWithBackButton
import kotlinx.coroutines.launch

@Composable
fun Settings(
    onDismissRequest: () -> Unit,
    hyprTrackerViewModel: HyprTrackerViewModel,
    medicineViewModel: MedicineViewModel,
    themeViewModel: ThemeViewModel
) {
    Dialog(onDismissRequest = onDismissRequest, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        val currentTheme by themeViewModel.themeMode.collectAsState()
        val showThemeDialog = remember { mutableStateOf(false) }
        val showLanguageDialog = remember { mutableStateOf(false) }
        val showClassificationTableDialog = remember { mutableStateOf(false) }
        val showDeleteBPDataDialog = remember { mutableStateOf(false) }
        val showDeleteMedicationDialog = remember { mutableStateOf(false) }
        val showBugReportDialog = remember { mutableStateOf(false) }
        val showAboutDialog = remember { mutableStateOf(false) }  
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        val showWarning = remember { mutableStateOf(false) }
        val uri = remember { mutableStateOf(Uri.EMPTY) }
        val importer = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){ fileUri -> if (fileUri != null){ showWarning.value = true; uri.value = fileUri } }
        val exporter = rememberLauncherForActivityResult(contract = ActivityResultContracts.CreateDocument("text/csv"), onResult = { uri -> })

        Card(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TitleBarWithBackButton(title = "Settings", onBackArrowClicked = { onDismissRequest() })

            //General Settings
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Title(title = "General")
            Option(
                title = "Theme",
                subtitle = when(currentTheme){
                    ThemeMode.LIGHT -> "Light"
                    ThemeMode.DARK -> "Dark"
                    ThemeMode.SYSTEM -> "System Default" },
                onClick = {showThemeDialog.value = true}
            )
            when{
                showThemeDialog.value -> Picker(
                    title = "Select theme",
                    options = mapOf(
                        "Dark" to {themeViewModel.setTheme(ThemeMode.DARK)},
                        "Light" to {themeViewModel.setTheme(ThemeMode.LIGHT)},
                        "System Default" to {themeViewModel.setTheme(ThemeMode.SYSTEM)}
                    ),
                    onDismissRequest = {showThemeDialog.value = false},
                    default = when (currentTheme) {
                        ThemeMode.LIGHT -> "Light"
                        ThemeMode.DARK -> "Dark"
                        ThemeMode.SYSTEM -> "System Default"
                    }
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Option(title = "Language", subtitle = "English (UK)") { showLanguageDialog.value = true }
            when{
                showLanguageDialog.value -> Picker(
                    title = "Select Language",
                    options = mapOf("English(UK)" to {}),
                    onDismissRequest = {showLanguageDialog.value = false},
                    default = "English(UK)"
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Option(title = "Classification table", subtitle = "International society of hypertension"){showClassificationTableDialog.value = true}
            when{
                showClassificationTableDialog.value -> Picker(
                    onDismissRequest = {showClassificationTableDialog.value = false},
                    title = "Select classification table",
                    options = mapOf("International society of hypertension" to {}),
                    default = "International society of hypertension"
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            //Data Settings
            Title(title = "Data")
            Option(title = "Delete BP data", subtitle = "Permanently delete all bp data") { showDeleteBPDataDialog.value = true }
            when{
                showDeleteBPDataDialog.value -> {
                    DeletionDialog(
                        onDismissRequest = {showDeleteBPDataDialog.value = false},
                        onDeleteRequest = {
                            scope.launch { hyprTrackerViewModel.deleteAllBPRecords() }
                            showDeleteBPDataDialog.value = false },
                        deletionText = "Doing this will permanently delete all logged BP readings on your device. Make sure to have backups of any important data"
                    )
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Option(title = "Delete medications?", subtitle = "Permanently delete all medication data") { showDeleteMedicationDialog.value = true }
            when{
                showDeleteMedicationDialog.value -> {
                    DeletionDialog(
                        onDismissRequest = {showDeleteMedicationDialog.value = false},
                        onDeleteRequest = {
                            scope.launch { medicineViewModel.deleteAllRecordedMedications()}
                            showDeleteMedicationDialog.value = false },
                        deletionText = "Doing this will permanently delete all saved medications on your device. Make sure to have backups of any important data"
                    )
                }
            }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            //Backup & restore settings
            Title("Backup & restore")
            Option(title = "Database export", subtitle = "Export all your logs in csv format") { exporter.launch("logs.csv") }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Option(title = "Database Import", subtitle = "Import a csv file containing your logs") { importer.launch("text/*") }
            when{
                showWarning.value ->
                    Dialog(onDismissRequest = {showWarning.value = false}) {
                        Card() {
                            Column( modifier = Modifier.padding(16.dp)){
                                Text(text = "This will override all your current logs. Is that okay?")
                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    TextButton(onClick = { onDismissRequest() }) { Text("Cancel") }
                                        TextButton(
                                            onClick = {
                                                scope.launch {
                                                hyprTrackerViewModel.importLogs(context.contentResolver.openInputStream(uri.value))
                                                onDismissRequest()
                                                }
                                            }
                                        ) {
                                            Text("Ok")
                                        }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 8.dp))

            //About section
            Title("About")
            Option(title = "About", subtitle = "Version 0.5.0") { showAboutDialog.value = true }
            when{showAboutDialog.value -> AboutDialog(onDismissRequest = {showAboutDialog.value = false}) }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            Option(title = "Report Bug", subtitle = "Report bugs found while using HyprTracker") { showBugReportDialog.value = true }
            when{showBugReportDialog.value -> BugReportDialog(onDismissRequest = {showBugReportDialog.value = false}) }
            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            //Info section
            Icon(
                painter = painterResource(R.drawable.ic_about),
                contentDescription = null,
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp)
            )
            Text(
                text = "HyprTracker is a mobile application designed to help users conveniently record and track their blood pressure readings. It does not provide medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional regarding any medical concerns or before making decisions about your health.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp)
            )
        }
    }
}

@Composable
fun Title(title: String){
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(start = 16.dp)
    )
}

@Composable
fun Option(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
            .clickable(onClick = {onClick()})
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}