package io.github.mcx360.hyprtracker.ui.mainScreen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.R
import io.github.mcx360.hyprtracker.ui.HyprTrackerViewModel
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.options.pickers.ClassificationTablePicker
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.options.dataDeletion.DeleteBPDataConfirmation
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.options.dataDeletion.DeleteMedicationsConfirmation
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.options.pickers.LanguagePicker
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.options.pickers.ThemePicker
import io.github.mcx360.hyprtracker.ui.mainScreen.settings.options.ThemeViewModel
import io.github.mcx360.hyprtracker.ui.medicineScreen.MedicineViewModel
import kotlinx.coroutines.launch

@Composable
fun Settings(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    hyprTrackerViewModel: HyprTrackerViewModel,
    medicineViewModel: MedicineViewModel,
    themeViewModel: ThemeViewModel
    ) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val showThemeDialog = remember { mutableStateOf(false) }
        val showLanguageDialog = remember { mutableStateOf(false) }
        val showClassificationTableDialog = remember { mutableStateOf(false) }
        val showDeleteBPDataDialog = remember { mutableStateOf(false) }
        val showDeleteMedicationDialog = remember { mutableStateOf(false) }
        val showHelpDialog = remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RectangleShape
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .systemBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDismissRequest) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text("General", style = MaterialTheme.typography.headlineMedium,color = MaterialTheme.colorScheme.secondary)
                    Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showThemeDialog.value = true})) {
                        Text("Theme", fontWeight = FontWeight.Bold)
                        Text(
                            text = "System Default",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        when{
                            showThemeDialog.value -> ThemePicker(
                                themeViewModel = themeViewModel,
                                onDismissRequest = {
                                showThemeDialog.value = false
                            })
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showLanguageDialog.value = true})) {
                        Text("Language", fontWeight = FontWeight.Bold)
                        Text(
                            text = "English (UK)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    when{
                        showLanguageDialog.value -> LanguagePicker(onDismissRequest = {
                            showLanguageDialog.value = false
                        })
                    }

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showClassificationTableDialog.value = true})) {
                        Text("Classification table", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Internation society of hypertension",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    when{
                        showClassificationTableDialog.value -> ClassificationTablePicker(onDismissRequest = {showClassificationTableDialog.value = false})
                    }

                    Text("Data", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp),color = MaterialTheme.colorScheme.secondary)

                    Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showDeleteBPDataDialog.value = true})) {
                        Text("Delete BP data", fontWeight = FontWeight.Bold)
                        Text(
                            "Permanently delete all bp data",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    when{
                        showDeleteBPDataDialog.value -> {
                            DeleteBPDataConfirmation(
                                onDismissRequest = {showDeleteBPDataDialog.value = false},
                                onDelete = {scope.launch { hyprTrackerViewModel.deleteAllBPRecords() } }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Column(modifier = modifier.fillMaxWidth().clickable(onClick = {showDeleteMedicationDialog.value = true})) {
                        Text("Delete medications?", fontWeight = FontWeight.Bold)
                        Text(
                            "Permanently delete all medication data",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    when{
                        showDeleteMedicationDialog.value -> {
                            DeleteMedicationsConfirmation(
                                onDismissRequest = {showDeleteMedicationDialog.value = false},
                                onDelete = {scope.launch {medicineViewModel.deleteAllRecordedMedications()}}
                            )
                        }
                    }

                    Text("About", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(top = 16.dp), color = MaterialTheme.colorScheme.secondary)

                    Column(modifier = modifier.fillMaxWidth().clickable(onClick = {})) {
                        Text(text = "Version", fontWeight = FontWeight.Bold)
                        Text(
                            text = "Version 0.5.0", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Spacer(modifier = Modifier.padding(vertical = 8.dp))

                    Column(modifier = modifier.fillMaxWidth().clickable(onClick = {})) {
                        Text("Help", fontWeight = FontWeight.Bold)
                        Text(
                            "FAQ help", style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
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
                }
            }
        }
    }
}