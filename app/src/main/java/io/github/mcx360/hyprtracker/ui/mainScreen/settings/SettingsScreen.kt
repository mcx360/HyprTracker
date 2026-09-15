package io.github.mcx360.hyprtracker.ui.mainScreen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.mainScreen.components.AboutDialog
import io.github.mcx360.hyprtracker.ui.mainScreen.components.BugReportDialog
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.components.Help
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.components.Picker
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import io.github.mcx360.hyprtracker.ui.theme.ThemeMode
import io.github.mcx360.hyprtracker.ui.utils.DeletionDialog
import io.github.mcx360.hyprtracker.ui.utils.TitleBarWithBackButton
import kotlinx.coroutines.launch

@Composable
fun Settings(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
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
        val showHelpDialog = remember { mutableStateOf(false) }
        val showBugReportDialog = remember { mutableStateOf(false) }
        val showAboutDialog = remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RectangleShape
        ) {
            TitleBarWithBackButton(
                title = "Settings",
                onBackArrowClicked = {onDismissRequest() }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = "General",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .clickable(onClick = {showThemeDialog.value = true})
                ) {
                    Text(
                        text = "Theme",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = when(currentTheme){
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.DARK -> "Dark"
                            ThemeMode.SYSTEM -> "System Default"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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

                Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showLanguageDialog.value = true})) {
                    Text(
                        text = "Language",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "English (UK)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                when{
                    showLanguageDialog.value -> Picker(
                        title = "Select Language",
                        options = mapOf("English(UK)" to {}),
                        onDismissRequest = {showLanguageDialog.value = false},
                        default = "English(UK)"
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showClassificationTableDialog.value = true})) {
                    Text(
                        text =  "Classification table",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Internation society of hypertension",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                when{
                    showClassificationTableDialog.value -> Picker(
                        onDismissRequest = {showClassificationTableDialog.value = false}, title = "Select classification table", options = mapOf("International society of hypertension" to {}), default = "International society of hypertension")
                }

                Text(
                    text = "Data",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.secondary
                )

                Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showDeleteBPDataDialog.value = true})) {
                    Text(
                        text = "Delete BP data",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Permanently delete all bp data",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                when{
                    showDeleteBPDataDialog.value -> {
                        DeletionDialog(
                            onDismissRequest = {showDeleteBPDataDialog.value = false},
                            onDeleteRequest = {
                                scope.launch { hyprTrackerViewModel.deleteAllBPRecords() }
                                showDeleteBPDataDialog.value = false
                            },
                            deletionText = "Doing this will permanently delete all logged BP readings on your device. Make sure to have backups of any important data"
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showDeleteMedicationDialog.value = true})) {
                    Text(
                        text = "Delete medications?",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Permanently delete all medication data",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                when{
                    showDeleteMedicationDialog.value -> {
                        DeletionDialog(
                            onDismissRequest = {showDeleteMedicationDialog.value = false},
                            onDeleteRequest = {
                                scope.launch { medicineViewModel.deleteAllRecordedMedications()}
                                showDeleteMedicationDialog.value = false
                            },
                            deletionText = "Doing this will permanently delete all saved medications on your device. Make sure to have backups of any important data"
                        )
                    }
                }

                Text(
                    text = "External",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showBugReportDialog.value = true})) {
                    Text(
                        text = "Report Bug",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Report bugs found while using HyprTracker",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,)
                }
                when{
                    showBugReportDialog.value -> BugReportDialog(onDismissRequest = {showBugReportDialog.value = false})
                }

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                Column(modifier = modifier.fillMaxWidth().clickable(onClick = {})) {
                    Text(
                        text = "Rate app",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Rate your experience of using HyprTracker",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "About",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.secondary
                )

                Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showAboutDialog.value = true})) {
                    Text(
                        text = "About",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Version 0.5.0", style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                when{
                    showAboutDialog.value -> AboutDialog(onDismissRequest = {showAboutDialog.value = false})
                }

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showHelpDialog.value = true})) {
                    Text(
                        text = "Help",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "FAQ help",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                when{
                    showHelpDialog.value -> Help(onDismissRequest = {showHelpDialog.value = false})
                }

                Spacer(modifier = Modifier.padding(vertical = 8.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_about),
                    contentDescription = null,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Text(
                    text = "HyprTracker is a mobile application designed to help users conveniently record and track their blood pressure readings. It does not provide medical advice, diagnosis, or treatment. Always consult a qualified healthcare professional regarding any medical concerns or before making decisions about your health.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
