package io.github.mcx360.hyprtracker.ui.medicineScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.mcx360.hyprtracker.ui.model.Medicine
import io.github.mcx360.hyprtracker.ui.utils.InfoDialog
import io.github.mcx360.hyprtracker.ui.utils.TitleBarWithBackButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    onDismissRequest: () -> Unit,
    snackBarHostState:  SnackbarHostState,
    scope: CoroutineScope,
    medicineViewModel: MedicineViewModel
){
    val haptic = LocalHapticFeedback.current
    val uiState = medicineViewModel.uiState.collectAsState()
    val errors = remember { mutableListOf(false, false, false, false) }
    var isMedicationDosePerIntakeInError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = {onDismissRequest()}, properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)) {
        Card{
            //Add Medication title
            TitleBarWithBackButton(title = "Add Medication", onBackArrowClicked = { onDismissRequest(); scope.launch { medicineViewModel.resetAddMedication(); medicineViewModel.fetchMedications() } })

            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "Medication Info",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(4.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary,
                        )

                        OutlinedTextField(
                            isError = errors[0],
                            onValueChange = { medicineViewModel.updateMedicationName(it); if (uiState.value.medicationName.isNotEmpty()) errors[0] = false },
                            value = uiState.value.medicationName,
                            label = { Text("Medication name*") },
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                            placeholder = { Text("e.g. Lisinopril") },
                            supportingText = { if (errors[0]) Text(text = "Medication name needed!", color = MaterialTheme.colorScheme.error)  else Text("*required") }
                        )

                        OutlinedTextField(
                            isError = errors[1],
                            onValueChange = { medicineViewModel.updateMedicationDescription(it); if (uiState.value.medicationDescription.isNotEmpty()) errors[1] = false },
                            value = uiState.value.medicationDescription,
                            label = { Text("Medication description*") },
                            maxLines = 1,
                            placeholder = { Text("e.g. Lowers high blood pressure") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                            supportingText = { if (errors[1])  Text(text = "Medication description needed!", color = MaterialTheme.colorScheme.error) else Text("*required")  }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val showScheduleDropDownMenu = remember { mutableStateOf(false) }
                        val showTimesPerDayDropDownMenu = remember { mutableStateOf(false) }
                        val showSelectedDaysPicker = remember { mutableStateOf(false) }
                        val showScheduleInfoDialog = remember { mutableStateOf(false) }
                        val showTimesPerDayInfoDialog = remember { mutableStateOf(false) }
                        val showDosePerIntakeInfoDialog = remember { mutableStateOf(false) }
                        //title
                        Text(
                            text = "Medication Schedule & Dosage",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(4.dp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            //Box for medication schedule
                            ExposedDropdownMenuBox(
                                expanded = showScheduleDropDownMenu.value,
                                onExpandedChange = { showScheduleDropDownMenu.value = true }) {
                                //Medication schedule field
                                OutlinedTextField(
                                    isError = errors[2],
                                    readOnly = true,
                                    onValueChange = {},
                                    value = uiState.value.medicationSchedule,
                                    label = { Text(text = "Schedule*") },
                                    placeholder = { Text(text = "e.g. Every day") },
                                    trailingIcon = {
                                        Icon(
                                            if (showScheduleDropDownMenu.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                            contentDescription = null
                                        )
                                    },
                                    maxLines = 1,
                                    modifier = Modifier.menuAnchor(
                                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                        enabled = true
                                    ),
                                )
                                //Medication schedule menu
                                ExposedDropdownMenu(
                                    expanded = showScheduleDropDownMenu.value,
                                    onDismissRequest = { showScheduleDropDownMenu.value = false }) {
                                    DropdownMenuItem(
                                        onClick = {
                                            medicineViewModel.updateMedicationSchedule("Every single day")
                                            errors[2] = false
                                            for (day in DayOfWeek.entries) {
                                                medicineViewModel.addSelectedDays(day.name)
                                            }
                                            showScheduleDropDownMenu.value = false
                                        },
                                        text = { Text(text = "Every single day") }
                                    )
                                    DropdownMenuItem(
                                        onClick = {
                                            errors[2] = false
                                            showScheduleDropDownMenu.value = false
                                            showSelectedDaysPicker.value = true
                                        },
                                        text = { Text(text = "On selected days only") }
                                    )
                                }
                            }
                            when {
                                showSelectedDaysPicker.value ->
                                    SelectDaysForMedication(
                                        onDismiss = {
                                            if (!it.isNullOrEmpty()) medicineViewModel.updateMedicationSchedule(
                                                it
                                            ); showSelectedDaysPicker.value = false
                                        },
                                        onDaySelected = { medicineViewModel.addSelectedDays(it) },
                                        onDayRemoved = { medicineViewModel.removeSelectedDays(it) }
                                    )
                            }

                            //Schedule info Dialog popup
                            IconButton(onClick = { showScheduleInfoDialog.value = true }) {
                                Icon(Icons.Default.Info, contentDescription = null)
                            }
                            if (showScheduleInfoDialog.value) {
                                InfoDialog(
                                    onDismissRequest = { showScheduleInfoDialog.value = false },
                                    info = "Schedule defines how often you take this medication (e.g. every day or on specific days).",
                                    title = "Schedule"
                                )
                            }
                        }

                        if (errors[2]) {
                            Text(
                                text = "medication scheduled intake is needed ",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            Text(
                                text = "*required",
                                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            //Box for times per day
                            ExposedDropdownMenuBox(
                                expanded = showTimesPerDayDropDownMenu.value,
                                onExpandedChange = { showTimesPerDayDropDownMenu.value = it }
                            ) {
                                //times per day field
                                OutlinedTextField(
                                    isError = errors[3],
                                    readOnly = true,
                                    onValueChange = {},
                                    value = when (uiState.value.medicationTimesPerDay) {
                                        0 -> ""
                                        1 -> "One time daily"
                                        2 -> "Two times daily"
                                        3 -> "Three times daily"
                                        4 -> "Four times daily"
                                        5 -> "Five times daily"
                                        6 -> "Six times daily"
                                        else -> ""
                                    },
                                    label = { Text(text = "Times per day*") },
                                    placeholder = { Text(text = "e.g. Once daily") },
                                    trailingIcon = {
                                        Icon(
                                            if (showTimesPerDayDropDownMenu.value) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                            null
                                        )
                                    },
                                    modifier = Modifier.menuAnchor(
                                        type = MenuAnchorType.PrimaryNotEditable,
                                        enabled = true
                                    ),
                                )

                                //times per day menu
                                ExposedDropdownMenu(
                                    expanded = showTimesPerDayDropDownMenu.value,
                                    onDismissRequest = { showTimesPerDayDropDownMenu.value = false }
                                ) {
                                    for (i in 1..6) {
                                        DropdownMenuItem(
                                            text = { Text("$i time(s) daily") },
                                            onClick = {
                                                errors[3] = false
                                                medicineViewModel.updateMedicationTimesPerDay(i)
                                                showTimesPerDayDropDownMenu.value = false
                                            }
                                        )
                                    }
                                }
                            }

                            //times per day info dialog popup
                            IconButton(onClick = { showTimesPerDayInfoDialog.value = true }) {
                                Icon(Icons.Default.Info, contentDescription = null)
                            }
                            if (showTimesPerDayInfoDialog.value) {
                                InfoDialog(
                                    info = "Times per day indicates how many times you take the medication on a scheduled day.",
                                    onDismissRequest = { showTimesPerDayInfoDialog.value = false },
                                    title = "Times Per Day"
                                )
                            }
                        }

                        if (errors[3]) {
                            Text(
                                text = "medication scheduled intake is needed ",
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            Text(
                                text = "*required",
                                modifier = Modifier.padding(start = 12.dp, top = 4.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }


                        Row(verticalAlignment = Alignment.CenterVertically) {
                            //Dose per intake
                            OutlinedTextField(
                                singleLine = true,
                                isError = isMedicationDosePerIntakeInError,
                                onValueChange = {
                                    medicineViewModel.updateMedicationDose(it); if (uiState.value.medicationDosage.isNotEmpty()) isMedicationDosePerIntakeInError =
                                    false
                                },
                                value = uiState.value.medicationDosage,
                                label = { Text(text = "Dose per Intake*") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                placeholder = { Text(text = "e.g. 1 x 10mg tablet") },
                                supportingText = { Text(text = if (isMedicationDosePerIntakeInError) "Dose per intake is needed" else "*required") }
                            )

                            //Dose per intake info popup
                            IconButton(onClick = { showDosePerIntakeInfoDialog.value = true }) {
                                Icon(Icons.Default.Info, contentDescription = null)
                            }

                            if (showDosePerIntakeInfoDialog.value) {
                                InfoDialog(
                                    info = "Dose per intake describes the amount of medication you take each time (e.g. 1 tablet or 10 mg).",
                                    onDismissRequest = {
                                        showDosePerIntakeInfoDialog.value = false
                                    },
                                    title = "Dose Per Intake"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Notification reminders Card
                NotificationsCard(
                    updateMedicationNotificationStatus = { medicineViewModel.updateMedicationNotificationStatus(it) },
                    updateMedicationReminderTime = { value, reminder -> medicineViewModel.updateMedicationReminderTime(value, reminder) },
                    medicationSchedule = uiState.value.medicationSchedule,
                    medicationSelectedDays = uiState.value.medicationSelectedDays,
                    medicationTimesPerDay = uiState.value.medicationTimesPerDay,
                    medicationReminderTimes = uiState.value.medicationReminderTimes
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Duration Card
                DurationCard(
                    startDate = uiState.value.date,
                    endDate = uiState.value.medicationEndDate,
                    updateMedicationEndDate = { medicineViewModel.updateMedicationEndDate(it) },
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Button(
                        modifier = Modifier.weight(1f).padding(16.dp),
                        onClick = {
                            when {uiState.value.medicationName.isEmpty() -> errors[0] = true }
                            when {uiState.value.medicationDescription.isEmpty() -> errors[1]= true }
                            when {uiState.value.medicationSchedule.isEmpty() ->  errors[2] = true }
                            when {uiState.value.medicationTimesPerDay == 0 ->  errors[3] = true }
                            when {uiState.value.medicationDosage.isEmpty() -> isMedicationDosePerIntakeInError = true }

                            if (errors[0] || errors[1] ||  errors[2] ||  errors[3] || isMedicationDosePerIntakeInError) {
                                haptic.performHapticFeedback(HapticFeedbackType.Reject)
                            } else {
                                haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                onDismissRequest()
                                scope.launch {
                                    medicineViewModel.addMedication()
                                    medicineViewModel.fetchMedications()
                                    snackBarHostState.showSnackbar(message = "Medication added", duration = SnackbarDuration.Short)
                                }
                            }
                        }
                    ) {
                        Text(text = "Add medication")
                    }
                }
            }
        }
    }
}